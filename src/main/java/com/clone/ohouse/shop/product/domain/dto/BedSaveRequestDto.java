package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Getter;

@Getter
public class BedSaveRequestDto {
    private ItemCategoryCode categoryCode;
    private String name;
    private String modelName;
    private String brandName;
    private String color;
    private String size;

    public BedSaveRequestDto(ItemCategoryCode categoryCode, String name, String modelName, String brandName, String color, String size) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
        this.color = color;
        this.size = size;
    }

    public Bed toEntity() {
        return Bed.builder()
                .name(name)
                .modelName(modelName)
                .brandName(brandName)
                .color(color)
                .size(size)
                .build();
    }
}
