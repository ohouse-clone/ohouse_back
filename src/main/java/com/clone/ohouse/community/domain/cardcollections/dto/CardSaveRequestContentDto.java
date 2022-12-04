package com.clone.ohouse.community.domain.cardcollections.dto;

import lombok.Getter;

@Getter
public class CardSaveRequestContentDto {
    private Integer sequence;
    private String content;

    public CardSaveRequestContentDto(Integer sequence, String content) {
        this.sequence = sequence;
        this.content = content;
    }

}
