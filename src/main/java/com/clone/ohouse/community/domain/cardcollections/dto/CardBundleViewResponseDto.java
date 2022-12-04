package com.clone.ohouse.community.domain.cardcollections.dto;

import lombok.Getter;

import java.util.List;

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
