package com.dku.council.domain.oauth.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class OauthTermsRequest {
    @NotBlank(message = "학번을 입력해주세요.")
    @Schema(description = "아이디(학번)", example = "12345678")
    private final String studentId;

    @NotBlank(message = "clientId를 입력해주세요.")
    private final String clientId;

    @NotBlank(message = "redirectUri를 입력해주세요.")
    private final String redirectUri;

    @NotBlank(message = "codeChallenge를 입력해주세요.")
    private final String codeChallenge;

    private final String codeChallengeMethod;

    private final String scope;

    @NotBlank(message = "responseType을 입력해주세요.")
    private final String responseType;

    public OauthInfo toOauthInfo() {
        return OauthInfo.of(clientId, redirectUri, codeChallenge, codeChallengeMethod, scope, responseType);
    }
}
