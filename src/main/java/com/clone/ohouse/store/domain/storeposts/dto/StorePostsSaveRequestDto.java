package com.clone.ohouse.store.domain.storeposts.dto;

import com.clone.ohouse.store.domain.storeposts.StorePosts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String contentUrl;
    @ApiModelProperty(
            value = "preview image 파일"
    )
    private String previewImageUrl;

    @ApiModelProperty(
            value = "작성자",
            required = true,
            example = "JJH"
    )
    private String author;


    public StorePostsSaveRequestDto(String title, String contentUrl, String previewImageUrl, String author) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.previewImageUrl = previewImageUrl;
        this.author = author;
    }

    public StorePosts toEntity() {
        return StorePosts.builder()
                .title(title)
                .contentUrl(contentUrl)
                .previewImageUrl(previewImageUrl)
                .author(author)
                .build();
    }

}
