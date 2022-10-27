package com.clone.ohouse.store.domain.item.dto;

import com.clone.ohouse.store.domain.item.Bed;
import lombok.Getter;

@Getter
public class BedSaveRequestDto {
    private String name;
    private String modelName;
    private String brandName;
    private String color;
    private String size;

    public BedSaveRequestDto(String name, String modelName, String brandName, String color, String size) {
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
