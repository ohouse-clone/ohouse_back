package com.clone.ohouse.shop.board.domain.dto;

import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@Getter
public class ProductBoardSaveRequestDto {
    private String title;
    private String content;
    private String author;
    private String modifiedUser;

    public ProductBoardSaveRequestDto(String title, String content, String author, String modifiedUser) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.modifiedUser = modifiedUser;
    }

    public ProductBoard toEntity(){
        return ProductBoard.builder()
                .title(title)
                .content(content.getBytes(StandardCharsets.UTF_8))
                .author(author)
                .modifiedUser(modifiedUser)
                .build();
    }

}
