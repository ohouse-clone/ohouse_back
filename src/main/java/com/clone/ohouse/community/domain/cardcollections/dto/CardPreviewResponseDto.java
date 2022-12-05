package com.clone.ohouse.community.domain.cardcollections.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "판매자가 게시글을 생성하기 위한 것입니다"
)
@NoArgsConstructor
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
