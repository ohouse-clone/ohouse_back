package com.clone.ohouse.shop.board.domain.dto;


import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ProductBoardResponseDto {
    private Long id;
    private boolean isActive;
    private String title;
    private byte[] content;
    private String author;
    private String modifiedUser;
    private boolean isDeleted;
    private Integer hit;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ProductBoardResponseDto(ProductBoard entity) {
        this.id = entity.getId();
        this.isActive = entity.isActive();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.modifiedUser = entity.getModifiedUser();
        this.isDeleted = entity.isDeleted();
        this.hit = entity.getHit();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
