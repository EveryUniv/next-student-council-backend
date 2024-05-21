package com.dku.council.domain.oauth.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class CodeChallengeConverter {
    public String convertToCodeChallenge(String code, String codeChallengeMethod) throws NoSuchAlgorithmException {
        return getCodeChallenge(code, codeChallengeMethod);
    }

    private static String getCodeChallenge(String codeVerifier, String codeChallengeMethod)
            throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(codeChallengeMethod);
            messageDigest.update(bytes, 0, bytes.length);
            byte[] digest = messageDigest.digest();
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(e);
        }
    }

}
