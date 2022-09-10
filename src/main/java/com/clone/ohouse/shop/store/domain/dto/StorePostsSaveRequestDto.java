package com.clone.ohouse.shop.store.domain.dto;

import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@ApiModel(
        value = "store post save request",
        description = "판매자가 게시글을 생성하기 위한 것입니다"
)
@NoArgsConstructor
@Getter
public class StorePostsSaveRequestDto {
    @ApiModelProperty(
            value = "store 글 제목",
            required = true,
            example = "임시 제목1")
    private String title;

    @ApiModelProperty(
            value = "내용, image 파일"
    )
    private byte[] content;
    @ApiModelProperty(
            value = "preview image 파일"
    )
    private byte[] previewImage;

    @ApiModelProperty(
            value = "작성자",
            required = true,
            example = "JJH"
    )
    private String author;


    public StorePostsSaveRequestDto(String title, byte[] content, byte[] previewImage, String author) {
        this.title = title;
        this.content = content;
        this.previewImage = previewImage;
        this.author = author;
    }

    public StorePosts toEntity() {
        return StorePosts.builder()
                .title(title)
                .content(content)
                .previewImage(previewImage)
                .author(author)
                .build();
    }

}
