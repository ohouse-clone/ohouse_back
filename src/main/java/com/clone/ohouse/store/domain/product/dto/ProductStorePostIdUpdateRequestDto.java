package com.clone.ohouse.store.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(
        description = "Product API(POST /store/api/v1/product/products)의 요청<br>" +
                "제품 여러개를 특정 store post에 등록 <br>" +
                "store post id와 product 의 각 id만으로 제품을 post에 등록합니다"
)
@NoArgsConstructor
@Getter
public class ProductStorePostIdUpdateRequestDto {
    @ApiModelProperty(
            value = "StorePost id",
            required = true
    )
    private Long storePostId;
    @ApiModelProperty(
            value = "등록할 product의 id들, product는 이미 등록되어 있어야 함",
            required = true
    )
    private List<Long> productIds;

    public ProductStorePostIdUpdateRequestDto(Long storePostId, List<Long> productIds) {
        this.storePostId = storePostId;
        this.productIds = productIds;
    }
}
