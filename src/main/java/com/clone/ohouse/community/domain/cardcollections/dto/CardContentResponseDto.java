package com.clone.ohouse.community.domain.cardcollections.dto;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@ApiModel(
        description = "게시글 내부, 사진+글 한 쌍을 의미합니다."
)
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
