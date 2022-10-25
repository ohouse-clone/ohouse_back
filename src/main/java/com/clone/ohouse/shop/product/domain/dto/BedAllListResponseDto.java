package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.Getter;

@Getter
public class BedAllListResponseDto {
    private final Long id;
    private final String name;
    private final String modelName;
    private final String brandName;
    private final String color;
    private final String size;

    public BedAllListResponseDto(Bed entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.modelName = entity.getModelName();
        this.brandName = entity.getBrandName();
        this.color = entity.getColor();
        this.size = entity.getSize();
    }
}
