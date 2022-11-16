package com.clone.ohouse.store.domain.item.bed.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.bed.Material;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StorageBedRequestDto extends ItemRequestDto {
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


    @Override
    public Item toEntity() {
        return new StorageBed(name, modelName, brandName, material);
    }
}
