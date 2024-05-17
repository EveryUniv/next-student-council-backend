package com.dku.council.domain.oauth.model.dto.request;

import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.dku.council.domain.oauth.model.entity.HashAlgorithm.SHA256;

@Getter
public class OauthInfo {
    private final String clientId;
    private final String redirectUri;
    private final String codeChallenge;
    private final String codeChallengeMethod;
    private final String scope;
    private final String responseType;

    private OauthInfo(String clientId, String redirectUri, String codeChallenge,
                      String codeChallengeMethod, String scope, String responseType) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
        this.scope = scope;
        this.responseType = responseType;
    }

    public static OauthInfo of(String clientId, String redirectUri, String codeChallenge,
                               String codeChallengeMethod, String scope, String responseType) {
        if (codeChallengeMethod == null) {
            codeChallengeMethod = SHA256.getShortenedAlgorithm();
        }
        return new OauthInfo(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);
    }

    public OauthCachePayload toCachePayload(Long userId) {
        return OauthCachePayload.of(userId, codeChallenge, codeChallengeMethod, scope);
    }

    public MultiValueMap<String, String> toQueryParams(String scope, String studentId, String applicationName) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("client_id", clientId);
        queryParams.add("redirect_uri", redirectUri);
        queryParams.add("response_type", responseType);
        queryParams.add("code_challenge", codeChallenge);
        queryParams.add("code_challenge_method", codeChallengeMethod);
        queryParams.add("scope", scope);
        queryParams.add("student_id", studentId);
        queryParams.add("application_name", applicationName);
        return queryParams;
    }

}
