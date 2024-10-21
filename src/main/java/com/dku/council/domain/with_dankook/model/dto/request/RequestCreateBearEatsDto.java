package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RequestCreateBearEatsDto extends RequestCreateWithDankookDto<BearEats>{

    @NotNull
    @Schema(description = "title", example = "베어이츠 제목")
    private final String title;

    @NotNull
    @Schema(description = "음식점", example = "피자헛")
    private final String restaurant;

    @NotNull
    @Schema(description = "배달 주문 장소", example = "피자헛")
    private final String deliveryPlace;

    @NotNull
    @Schema(description = "배달 시간", example = "2023-12-25 17:30", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime deliveryTime;

    @NotBlank
    @Schema(description = "본문", example = "내용")
    private final String body;

    @NotNull
    @Schema(description = "카카오톡 오픈채팅 링크", example = "https://open.kakao.com/o/gjgjgjgj")
    private final String kakaoOpenChatLink;

    @Override
    public BearEats toEntity(User user) {
        return BearEats.builder()
                .user(user)
                .title(title)
                .restaurant(restaurant)
                .deliveryPlace(deliveryPlace)
                .deliveryTime(deliveryTime)
                .content(body)
                .kakaoOpenChatLink(kakaoOpenChatLink)
                .build();
    }
}
