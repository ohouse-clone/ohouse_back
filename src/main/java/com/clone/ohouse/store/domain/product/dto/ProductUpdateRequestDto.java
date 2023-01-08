package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Product API(PUT /store/api/v1/product/{id})의 요청<br>" +
                "등록된 제품을 수정하는데 사용합니다 <br>" +
                "사용하지 않는 필드는 NULL을, 수정이 필요한 필드만 사용하세요. 모두를 채울 필요는 없습니다"
)
@NoArgsConstructor
@Getter
public class ProductUpdateRequestDto {
    @ApiModelProperty(
            value = "item id",
            required = false
    )
    private Long itemId;

    @ApiModelProperty(
            value = "제품 이름",
            required = false
    )
    private String productName;
    @ApiModelProperty(
            value = "제품 가격",
            required = false
    )
    private Long price;
    @ApiModelProperty(
            value = "제품 수량",
            required = false
    )
    private Long stock;
    @ApiModelProperty(
            value = "할인율",
            required = false
    )
    private Integer rateDiscount;
    @ApiModelProperty(
            value = "등록된 post id",
            required = false
    )
    private Long storePostId;

    @Builder
    public ProductUpdateRequestDto(Long itemId, String productName, Long price, Long stock, Integer rateDiscount, Long storePostId) {
        this.itemId = itemId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.storePostId = storePostId;
    }
}
