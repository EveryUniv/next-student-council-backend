package com.dku.council.domain.with_dankook.model.dto.response;

import com.dku.council.domain.with_dankook.model.WithDankookStatus;
import com.dku.council.domain.with_dankook.model.dto.RecruitedUsersDto;
import com.dku.council.domain.with_dankook.model.entity.type.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResponseSingleStudyDto extends ResponseSingleWithDankookDto {

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "최소 학번", example = "19")
    private final int minStudentId;

    @Schema(description = "스터디 총 기간", example = "2024-01-01 ~ 2024-06-01")
    private final String studyDate;

    @Schema(description = "해시태그")
    private final String tag;

    @Schema(description = "내용", example = "게시글 본문")
    private final String body;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    @Schema(description = "모집된 사용자들")
    private final List<RecruitedUsersDto> recruitedUsers;

    @Schema(description = "카카오 오픈채팅방 URL")
    private final String kakaoOpenChatLink;

    @Schema(description = "내가 참여했는지 여부", example = "true")
    private final boolean isApplied;

    public ResponseSingleStudyDto(ResponseSingleWithDankookDto dto, Study study, int recruitedCount, boolean isApplied) {
        super(dto);
        this.title = study.getTitle();
        this.minStudentId = study.getMinStudentId();
        this.studyDate = changeDate(study.getStartTime(), study.getEndTime());
        this.body = study.getContent();
        this.tag = study.getTag().getName();
        this.recruitedCount = recruitedCount;
        this.recruitedUsers = study.getUsers().stream()
                .map(RecruitedUsersDto::new)
                .collect(Collectors.toList());
        this.kakaoOpenChatLink = (study.getStatus().equals(WithDankookStatus.CLOSED.toString())) ? study.getKakaoOpenChatLink() : null;
        this.isApplied = isApplied;
    }

    private String changeDate(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return startDate.format(formatter) + " ~ " + endDate.format(formatter);
    }
}
