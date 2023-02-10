package com.clone.ohouse.store.domain.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BedUpdateRequestDto {
    private String name;
    private String modelName;
    private String brandName;
    private String color;
    private String size;

    @Builder
    public BedUpdateRequestDto(String name, String modelName, String brandName, String color, String size) {
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
        this.color = color;
        this.size = size;
    }
}
