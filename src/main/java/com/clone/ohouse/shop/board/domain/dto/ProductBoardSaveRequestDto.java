package com.clone.ohouse.shop.board.domain.dto;

import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@ApiModel(
        value = "store post save request",
        description = "판매자가 게시글을 생성하기 위한 것입니다"
)
@NoArgsConstructor
@Getter
public class ProductBoardSaveRequestDto {
    @ApiModelProperty(
            value = "store 글 제목",
            required = true,
            example = "임시 제목1")
    private String title;

    @ApiModelProperty(
            value = "글 내용, 내용 형태는 html 문서",
            example = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head></head>" +
                    "<body></body>" +
                    "</html>")
    private String content;
    @ApiModelProperty(
            value = "작성자",
            required = true,
            example = "JJH"
    )
    private String author;


    public ProductBoardSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public ProductBoard toEntity() {
        return ProductBoard.builder()
                .title(title)
                .content(content.getBytes(StandardCharsets.UTF_8))
                .author(author)
                .build();
    }

}
