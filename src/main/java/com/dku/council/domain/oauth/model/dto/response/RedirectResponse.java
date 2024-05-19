package com.dku.council.domain.oauth.model.dto.response;

import lombok.Getter;

@Getter
public class RedirectResponse {
    private final String redirectUri;

    private RedirectResponse(String uri) {
        this.redirectUri = uri;
    }

    public static RedirectResponse from(String uri) {
        return new RedirectResponse(uri);
    }
}
