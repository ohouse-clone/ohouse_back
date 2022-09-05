package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Furniture;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Getter;

@Getter
public class FurnitureAllListResponseDto {
    private final Long id;
    private final ItemCategoryCode categoryCode;
    private final String name;
    private final String modelName;
    private final String brandName;
    private final String color;
    private final String size;

    public FurnitureAllListResponseDto(Furniture entity) {
        this.id = entity.getId();
        this.categoryCode = entity.getCategoryCode();
        this.name = entity.getName();
        this.modelName = entity.getModelName();
        this.brandName = entity.getBrandName();
        this.color = entity.getColor();
        this.size = entity.getSize();
    }
}
