package com.dku.council.domain.oauth.service;

import com.dku.council.domain.oauth.exception.*;
import com.dku.council.domain.oauth.model.dto.request.*;
import com.dku.council.domain.oauth.model.dto.response.RedirectResponse;
import com.dku.council.domain.oauth.model.dto.response.TokenExchangeResponse;
import com.dku.council.domain.oauth.model.entity.*;
import com.dku.council.domain.oauth.repository.OauthClientRepository;
import com.dku.council.domain.oauth.repository.OauthConnectionRepository;
import com.dku.council.domain.oauth.repository.OauthRedisRepository;
import com.dku.council.domain.oauth.util.CodeChallengeConverter;
import com.dku.council.domain.user.exception.WrongPasswordException;
import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.user.util.CodeGenerator;
import com.dku.council.global.auth.jwt.AuthenticationToken;
import com.dku.council.global.auth.jwt.JwtProvider;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {
    private final OauthClientRepository oauthClientRepository;
    private final OauthRedisRepository oauthRedisRepository;
    private final OauthConnectionRepository oauthConnectionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeChallengeConverter codeChallengeConverter;
    private final JwtProvider jwtProvider;
    @Value("${app.oauth.login.url}")
    private final String LOGIN_URL;
    @Value("${app.oauth.terms.url}")
    private final String TERMS_URL;
    private final String CODE = "code";

    public RedirectResponse authorize(OauthRequest oauthRequest) {
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectUri();
        checkResponseType(oauthRequest.getResponseType());

        OauthClient oauthClient = getOauthClient(clientId);
        oauthClient.checkClientId(clientId);
        oauthClient.checkRedirectUri(redirectUri);

        String uri = UriComponentsBuilder
                .fromUriString(LOGIN_URL)
                .queryParams(oauthRequest.toQueryParams())
                .toUriString();

        return RedirectResponse.from(uri);
    }

    @Transactional
    public RedirectResponse login(RequestLoginDto loginInfo, OauthInfo oauthInfo) {
        checkResponseType(oauthInfo.getResponseType());
        User user = userRepository.findByStudentId(loginInfo.getStudentId()).orElseThrow(UserNotFoundException::new);
        checkPassword(loginInfo.getPassword(), user.getPassword());

        OauthClient oauthClient = getOauthClient(oauthInfo.getClientId());
        Optional<OauthConnection> connectionOptional =
                oauthConnectionRepository.findByUserAndOauthClient(user, oauthClient);

        if (isDisconnected(connectionOptional)) {
            MultiValueMap<String, String> params = oauthInfo.
                    toQueryParams(oauthClient.getScope(), user.getStudentId(), oauthClient.getApplicationName());
            String uri = UriComponentsBuilder
                    .fromUriString(TERMS_URL)
                    .queryParams(params)
                    .toUriString();
            return RedirectResponse.from(uri);
        }

        return redirectWithAuthCode(oauthInfo, user, oauthClient);
    }

    @Transactional
    public String verifyTerms(String studentId, OauthInfo oauthInfo) {
        checkResponseType(oauthInfo.getResponseType());
        User user = userRepository.findByStudentId(studentId).orElseThrow(UserNotFoundException::new);
        OauthClient oauthClient = getOauthClient(oauthInfo.getClientId());
        oauthClient.checkRedirectUri(oauthInfo.getRedirectUri());
        return redirectWithAuthCode(oauthInfo, user, oauthClient);
    }

    public TokenExchangeResponse exchangeToken(ClientInfo clientInfo, OAuthTarget target) {
        checkGrantType(target.getGrantType());
        OauthClient oauthClient = getOauthClient(clientInfo.getClientId());
        oauthClient.checkClientSecret(clientInfo.getClientSecret());

        OauthCachePayload payload = getPayload(target);
        String codeVerifier = target.getCodeVerifier();
        String codeChallengeMethod = getCodeChallengeMethod(payload.getCodeChallengeMethod());
        checkCodeChallenge(codeVerifier, codeChallengeMethod, payload);

        User user = userRepository.findById(payload.getUserId()).orElseThrow(UserNotFoundException::new);
        AuthenticationToken token = jwtProvider.issue(user);

        return TokenExchangeResponse.of(token.getAccessToken(), token.getRefreshToken(), payload.getScope());
    }

    @NotNull
    private String redirectWithAuthCode(OauthInfo oauthInfo, User user, OauthClient oauthClient) {
        String authCode = CodeGenerator.generateUUIDCode();

        OauthCachePayload cachePayload = oauthInfo.toCachePayload(user.getId(), oauthClient.getScope());
        oauthRedisRepository.cacheOauth(authCode, cachePayload);

        oauthConnectionRepository.findByUserAndOauthClient(user, oauthClient).orElseGet(() -> {
            OauthConnection oauthConnection = OauthConnection.of(user, oauthClient);
            return oauthConnectionRepository.save(oauthConnection);
        });

        return UriComponentsBuilder
                .fromUriString(oauthInfo.getRedirectUri())
                .queryParam(CODE, authCode)
                .toUriString();
    }

    private static boolean isDisconnected(Optional<OauthConnection> connectionOptional) {
        OauthConnection connection = connectionOptional.orElse(null);
        return connectionOptional.isEmpty() || connection.getStatus() == ConnectionStatus.DISCONNECTED;
    }

    private void checkPassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new WrongPasswordException();
        }
    }

    private String getCodeChallengeMethod(String codeChallengeMethod) {
        if (codeChallengeMethod == null) {
            return HashAlgorithm.SHA256.getAlgorithm();
        }
        if (Objects.equals(codeChallengeMethod, HashAlgorithm.SHA1.getShortenedAlgorithm())) {
            codeChallengeMethod = HashAlgorithm.SHA1.getAlgorithm();
        } else if (Objects.equals(codeChallengeMethod, HashAlgorithm.SHA256.getShortenedAlgorithm())) {
            codeChallengeMethod = HashAlgorithm.SHA256.getAlgorithm();
        } else if (Objects.equals(codeChallengeMethod, HashAlgorithm.SHA512.getShortenedAlgorithm())) {
            codeChallengeMethod = HashAlgorithm.SHA512.getAlgorithm();
        }
        return codeChallengeMethod;
    }

    private void checkCodeChallenge(String codeVerifier, String codeChallengeMethod, OauthCachePayload payload) {
        String convertedCode = codeChallengeConverter.convertToCodeChallenge(codeVerifier, codeChallengeMethod);
        payload.checkCodeChallenge(convertedCode);
    }

    private OauthCachePayload getPayload(OAuthTarget target) {
        return oauthRedisRepository.getOauth(target.getCode())
                .orElseThrow(OauthCacheNotFoundException::new);
    }

    private OauthClient getOauthClient(String clientId) {
        return oauthClientRepository.findByClientId(clientId)
                .orElseThrow(OauthClientNotFoundException::new);
    }

    private void checkResponseType(String responseType) {
        String type = OauthResponseType.CODE.getValue();
        if (!responseType.equals(type)) {
            throw new InvalidOauthResponseTypeException(responseType);
        }
    }

    private void checkGrantType(String grantType) {
        if (!grantType.equals("authorization_code")) {
            throw new InvalidGrantTypeException(grantType);
        }
    }

}
