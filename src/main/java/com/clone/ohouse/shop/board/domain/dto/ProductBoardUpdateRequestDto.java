package com.clone.ohouse.shop.board.domain.dto;

import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductBoardUpdateRequestDto {
    private String title;
    private byte[] content;
    private String modifiedUser;

    private boolean isActive;
    private boolean isDeleted;

    @Builder
    public ProductBoardUpdateRequestDto(String title, byte[] content, String modifiedUser, boolean isActive, boolean isDeleted) {
        this.title = title;
        this.content = content;
        this.modifiedUser = modifiedUser;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }
}
