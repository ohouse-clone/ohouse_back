package com.clone.ohouse.store.domain.item.bed.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSize;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BedRequestDto extends ItemRequestDto {
    private Long id;

    private String name;
    private String modelName;
    private String brandName;
    private BedColor color;
    private BedSize size;


    public BedRequestDto(Bed entity) {
        this.id = entity.getId();
        this.color = entity.getColor();
        this.size = entity.getSize();
        this.name = entity.getName();
        this.brandName = entity.getBrandName();
        this.modelName = entity.getModelName();
    }

    public BedRequestDto(String name, String modelName, String brandName, BedColor color, BedSize size) {
        this.color = color;
        this.size = size;
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
    }

    @Override
    public Item toEntity() {
        return new Bed(name, modelName, brandName, size, color);
    }
}
