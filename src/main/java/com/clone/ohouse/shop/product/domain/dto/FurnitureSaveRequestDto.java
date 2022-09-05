package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Furniture;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Getter;

@Getter
public class FurnitureSaveRequestDto {
    private ItemCategoryCode categoryCode;
    private String name;
    private String modelName;
    private String brandName;
    private String color;
    private String size;

    public FurnitureSaveRequestDto(ItemCategoryCode categoryCode, String name, String modelName, String brandName, String color, String size) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
        this.color = color;
        this.size = size;
    }

    public Furniture toEntity() {
        return Furniture.builder()
                .categoryCode(categoryCode)
                .name(name)
                .modelName(modelName)
                .brandName(brandName)
                .color(color)
                .size(size)
                .build();
    }
}
