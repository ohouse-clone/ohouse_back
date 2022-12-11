package com.clone.ohouse.store.domain.storeposts.dto;

import com.clone.ohouse.store.domain.storeposts.StorePosts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
@ApiModel(
        description = "Store API (POST /store/api/v1/post/{id}) 요청<br>" +
                "판매자가 게시글을 생성하기 위한 것입니다"
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
            value = "내용, image 파일 Id"
    )
    private Long contentImageId;
    @ApiModelProperty(
            value = "preview image 파일 Id"
    )
    private Long previewImageId;

    @ApiModelProperty(
            value = "작성자",
            required = true,
            example = "JJH"
    )
    private String author;


    public StorePostsSaveRequestDto(String title, Long contentImageId, Long previewImageId, String author) {
        this.title = title;
        this.contentImageId = contentImageId;
        this.previewImageId = previewImageId;
        this.author = author;
    }

}
