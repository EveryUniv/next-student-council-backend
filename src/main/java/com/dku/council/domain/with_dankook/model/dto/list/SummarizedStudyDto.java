package com.dku.council.domain.with_dankook.model.dto.list;

import com.dku.council.domain.with_dankook.model.entity.type.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
public class SummarizedStudyDto extends SummarizedWithDankookDto {

    @Schema(description = "제목", example = "게시글 제목")
    private final String title;

    @Schema(description = "내용", example = "게시글 본문")
    private final String body;

    @Schema(description = "해시태그")
    private final String tag;

    @Schema(description = "상태")
    private final String status;

    @Schema(description = "모집된 인원", example = "1")
    private final int recruitedCount;

    public SummarizedStudyDto(SummarizedWithDankookDto dto, Study study, int recruitedCount, MessageSource messageSource) {
        super(dto);
        this.title = study.getTitle();
        this.body = study.getContent();
        this.tag = study.getTag().getName();
        this.status = messageSource.getMessage("witdankook." + study.getStatus().toLowerCase(), new Object[]{}, LocaleContextHolder.getLocale());
        this.recruitedCount = recruitedCount;
    }
}
