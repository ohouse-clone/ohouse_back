package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Getter;

@Getter
public class ItemCategoryListResponseDto {
    private final Long categoryId;
    private final String categoryDetail;
    private final Integer category1;
    private final Integer category2;
    private final Integer category3;
    private final Integer category4;

    public ItemCategoryListResponseDto(ItemCategoryCode entity) {
        this.categoryId = entity.getId();
        this.categoryDetail = entity.getCategoryDetail();
        this.category1 = entity.getCategory1();
        this.category2 = entity.getCategory2();
        this.category3 = entity.getCategory3();
        this.category4 = entity.getCategory4();
    }
}
