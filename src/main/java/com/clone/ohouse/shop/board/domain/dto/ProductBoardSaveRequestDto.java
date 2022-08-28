package com.clone.ohouse.shop.board.domain.dto;

import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductBoardSaveRequestDto {
    private String title;
    private byte[] content;
    private String author;
    private String modifiedUser;

    public ProductBoardSaveRequestDto(String title, byte[] content, String author, String modifiedUser) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.modifiedUser = modifiedUser;
    }

    public ProductBoard toEntity(){
        return ProductBoard.builder()
                .title(title)
                .content(content)
                .author(author)
                .modifiedUser(modifiedUser)
                .build();
    }
}
