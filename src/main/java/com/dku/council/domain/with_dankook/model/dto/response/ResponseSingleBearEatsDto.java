package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.WithDankookStatus;
import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseSingleBearEatsDto extends ResponseSingleWithDankookDto {

    @Schema(description = "제목", example = "제목")
    private final String title;

    @Schema(description = "식당 이름", example = "피자헛")
    private final String restaurant;

    @Schema(description = "배달 주문 장소", example = "피자헛")
    private final String deliveryPlace;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "배달 시간", example = "2021-01-01 12:00")
    private final String deliveryTime;

    @Schema(description = "내용", example = "피자헛에서 피자를 시켜먹을 사람을 구합니다.")
    private final String body;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    @Schema(description = "모집된 사용자들")
    private final List<RecruitedUsersDto> recruitedUsers;

    @Schema(description = "카카오 오픈채팅방 URL")
    private final String kakaoOpenChatLink;

    @Schema(description = "내가 신청했는지 여부", example = "true")
    private final boolean isApplied;

    public ResponseSingleBearEatsDto(ResponseSingleWithDankookDto dto, BearEats bearEats, int recruitedCount, boolean isApplied) {
        super(dto);
        this.title = bearEats.getTitle();
        this.restaurant = bearEats.getRestaurant();
        this.deliveryPlace = bearEats.getDeliveryPlace();
        this.deliveryTime = bearEats.getDeliveryTime().toString();
        this.body = bearEats.getContent();
        this.recruitedCount = recruitedCount;
        this.recruitedUsers = bearEats.getUsers().stream()
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
        this.kakaoOpenChatLink = (bearEats.getStatus().equals(WithDankookStatus.CLOSED.toString())) ? bearEats.getKakaoOpenChatLink() : null;
        this.isApplied = isApplied;
    }
}
