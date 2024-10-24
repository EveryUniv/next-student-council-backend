package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidTimeException extends LocalizedMessageException {
    public InvalidTimeException() {
        super(HttpStatus.FORBIDDEN, "invalid.time");
    }
}
