package com.clone.ohouse.store.domain.item.bed.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        value = "Bed 에 대한 request",
        description = "저장, 수정, 조회 등에서 다양하게 사용됩니다<br>" +
                "수정의 경우는 수정이 필요한 일부분만 수정하세요. 전체 property를 모두 채울 필요는 없습니다."
)
@NoArgsConstructor
@Getter
public class BedRequestDto extends ItemRequestDto {
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
            value = "침대 색상(RED, BLUE, WHITE 중 하나)",
            required = true
    )
    private BedColor color;

    @ApiModelProperty(
            value = "침대 사이즈(MS, S, SS, D, Q, K, LK, CK 중 하나)",
            required = true
    )
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
