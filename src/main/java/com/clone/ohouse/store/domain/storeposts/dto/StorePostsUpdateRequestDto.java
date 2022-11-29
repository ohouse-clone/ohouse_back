package com.clone.ohouse.store.domain.storeposts.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Api
@ApiModel(
        value = "store post update request",
        description = "modifiedUser를 제외하고 모든 properties가 채워질 필요가 없습니다, 수정할 것만 채우면 됩니다. modifiedUser는 반드시 채워져야합니다."
)
@NoArgsConstructor
@Getter
@Setter
public class StorePostsUpdateRequestDto {
    @ApiModelProperty(
            value = "store 게시글 제목",
            required = false,
            example = "바뀐 제목1"
    )
    private String title;

    @ApiModelProperty(
            value = "내용, image id"
    )
    private Long contentImageId;
    @ApiModelProperty(
            value = "preview image id"
    )
    private Long previewImageId;
    @ApiModelProperty(
            value = "수정한 자",
            required = true,
            example = "LMA"
    )
    private String modifiedUser;

    @ApiModelProperty(
            value = "게시글을 활성화(보이도록)할지 비활성화 할지 결정하는 properties입니다.",
            dataType = "boolean",
            example = "false"
    )
    private boolean isActive;
    @ApiModelProperty(
            value = "게시글을 삭제 여부를 결정하는 properties입니다. (default is false)",
            dataType = "boolean",
            example = "false"
    )
    private boolean isDeleted = false;

    @Builder
    public StorePostsUpdateRequestDto(String title, Long contentImageId, Long previewImageId, String modifiedUser, boolean isActive, boolean isDeleted) {
        this.title = title;
        this.contentImageId = contentImageId;
        this.previewImageId = previewImageId;
        this.modifiedUser = modifiedUser;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }
}
