package com.clone.ohouse.community.domain.cardcollections.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@ApiModel(
        description = "호출된 preview card를 전체 개수, 개수, preview dto로 보여줍니다."
)
@Getter
public class CardBundleViewResponseDto {
    private Long totalNum;
    private Long cardNum;

    private List<CardPreviewResponseDto> previewCards;

    public CardBundleViewResponseDto(Long totalNum, Long cardNum, List<CardPreviewResponseDto> previewCards) {
        this.totalNum = totalNum;
        this.cardNum = cardNum;
        this.previewCards = previewCards;
    }
}
