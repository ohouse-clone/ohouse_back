package com.clone.ohouse.community.domain.cardcollections.dto;

import lombok.Getter;

@Getter
public class CardPreviewResponseDto {
    private String previewImageUrl;
    private Long hit;
    private Long commentNum;
    private String previewContent;
    private String famousComment;
    private String commentWriter;

    public CardPreviewResponseDto(String previewImageUrl, Long hit, Long commentNum, String previewContent, String famousComment, String commentWriter) {
        this.previewImageUrl = previewImageUrl;
        this.hit = hit;
        this.commentNum = commentNum;
        this.previewContent = previewContent;
        this.famousComment = famousComment;
        this.commentWriter = commentWriter;
    }
}
