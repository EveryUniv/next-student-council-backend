package com.dku.council.domain.with_dankook.exception;

import com.dku.council.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class CannotEnterDueToTimeException extends LocalizedMessageException {
    public CannotEnterDueToTimeException() {
        super(HttpStatus.FORBIDDEN, "cannot.enter.due-to-time");
    }
}
