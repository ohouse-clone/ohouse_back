package com.clone.ohouse.community.domain.cardcollections.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CardContentResponseDto {
    private String content;
    private String fileUrl;

    public CardContentResponseDto(String content, String fileUrl) {
        this.content = content;
        this.fileUrl = fileUrl;
    }
}
