package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Getter;

@Getter
public class ItemAllListResponseDto {
    private final Long itemId;
    private final ItemCategoryCode categoryCode;
    private final String name;
    private final String modelName;
    private final String brandName;

    public ItemAllListResponseDto(Item entity) {
        this.itemId = entity.getId();
        this.categoryCode = entity.getCategoryCode();
        this.name = entity.getName();
        this.modelName = entity.getModelName();
        this.brandName = entity.getBrandName();
    }
}
