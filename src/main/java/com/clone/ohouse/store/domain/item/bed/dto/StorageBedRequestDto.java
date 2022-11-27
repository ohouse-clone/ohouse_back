package com.clone.ohouse.store.domain.item.bed.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.bed.Material;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
@ApiModel(
        value = "StorageBed 에 대한 request",
        description = "저장, 수정, 조회 등에서 다양하게 사용됩니다<br>" +
                "수정의 경우는 수정이 필요한 일부분만 수정하세요. 전체 property를 모두 채울 필요는 없습니다."
)
@NoArgsConstructor
@Getter
public class StorageBedRequestDto extends ItemRequestDto {
    private Long id;
    @ApiModelProperty(
            value = "item 이름",
            required = true
    )
    private String name;
    @ApiModelProperty(
            value = "item modelName",
            required = true
    )
    private String modelName;
    @ApiModelProperty(
            value = "item brandName",
            required = true
    )
    private String brandName;
    @ApiModelProperty(
            value = "침대 재료<br>" + "(one of) WOOD, STEEL, FAKE_LEATHER, FAKE_WOOD",
            required = true
    )
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
