package com.clone.ohouse.store.domain.item.bed.dto;

import com.clone.ohouse.store.domain.item.bed.Material;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import lombok.Getter;

@Getter
public class StorageBedRequestDto {
    private Long id;
    private String name;
    private String modelName;
    private String brandName;
    private Material material;

    public StorageBedRequestDto(StorageBed entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.modelName = entity.getModelName();
        this.brandName = entity.getBrandName();
        this.material = entity.getMaterial();
    }

    public StorageBedRequestDto(String name, String modelName, String brandName, Material material) {
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
        this.material = material;
    }

    public StorageBed toEntity(){
        return new StorageBed(name, modelName, brandName, material);
    }
}
