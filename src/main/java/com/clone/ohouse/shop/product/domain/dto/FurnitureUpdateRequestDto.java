package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FurnitureUpdateRequestDto {
    private ItemCategoryCode categoryCode;
    private String name;
    private String modelName;
    private String brandName;
    private String color;
    private String size;

    @Builder
    public FurnitureUpdateRequestDto(ItemCategoryCode categoryCode, String name, String modelName, String brandName, String color, String size) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
        this.color = color;
        this.size = size;
    }
}
