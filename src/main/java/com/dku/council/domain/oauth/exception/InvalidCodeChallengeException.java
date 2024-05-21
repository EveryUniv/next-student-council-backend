package com.dku.council.domain.oauth.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidCodeChallengeException extends LocalizedMessageException {
    public InvalidCodeChallengeException(String codeVerifier, String codeChallenge) {
        super(HttpStatus.BAD_REQUEST, "codeVerifier: " + codeVerifier+ ", codeChallenge: " + codeChallenge);
    }
}
