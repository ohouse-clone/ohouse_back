package com.clone.ohouse.shop.board.domain.dto;

import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
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
public class ProductBoardUpdateRequestDto {
    @ApiModelProperty(
            value = "store 게시글 제목",
            required = false,
            example = "바뀐 제목1"
    )
    private String title;

    @ApiModelProperty(
            value = "글 내용, 내용 형태는 html 문서",
            example = "<html></html>"
    )
    private String content;
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
    public ProductBoardUpdateRequestDto(String title, String content, String modifiedUser, boolean isActive, boolean isDeleted) {
        this.title = title;
        this.content = content;
        this.modifiedUser = modifiedUser;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }
}
