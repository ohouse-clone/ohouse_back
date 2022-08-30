package com.clone.ohouse.shop.board.domain.dto;


import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
@ApiModel(
        value = "store post response",
        description = "게시글의 모든 내용을 담고 있습니다. 모든 내용이 ReadOnly입니다."
)
@AllArgsConstructor
@Getter
public class ProductBoardResponseDto {

    @ApiModelProperty(
            value = "store 게시글 id",
            required = true,
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
            )
    private Long id;

    @ApiModelProperty(
            value = "store 글 제목",
            required = true
    )
    private String title;
    @ApiModelProperty(
            value = "store 글 내용, 내용 형태는 html 문서",
            example = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head></head>" +
                    "<body></body>" +
                    "</html>"
    )
    private String content;

    @ApiModelProperty(
            value = "작성자",
            required = true
    )
    private String author;

    @ApiModelProperty(
            value = "수정한 자, 수정한 적이 없다면 null입니다."
    )
    private String modifiedUser;

    @ApiModelProperty(
            value = "조회수",
            allowableValues = "[0, infinity]"
    )
    private Integer hit;

    @ApiModelProperty(
            value = "최초 생성일",
            required = true

    )
    private LocalDateTime createdDate;

    @ApiModelProperty(
            value = "수정 일"
    )
    private LocalDateTime modifiedDate;

    @ApiModelProperty(
            value = "활성화 여부, 게시글이 활성화(보여질)지 아닐지 결정합니다.",
            example = "false"
    )
    private boolean isActive;
    @ApiModelProperty(
            value = "삭제 여부, 게시글이 삭제되었는지 아닌지 결정합니다.",
            example = "false"
    )
    private boolean isDeleted;


    public ProductBoardResponseDto(ProductBoard entity) {
        this.id = entity.getId();
        this.isActive = entity.isActive();
        this.title = entity.getTitle();
        this.content = new String(entity.getContent(), StandardCharsets.UTF_8);
        this.author = entity.getAuthor();
        this.modifiedUser = entity.getModifiedUser();
        this.isDeleted = entity.isDeleted();
        this.hit = entity.getHit();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
