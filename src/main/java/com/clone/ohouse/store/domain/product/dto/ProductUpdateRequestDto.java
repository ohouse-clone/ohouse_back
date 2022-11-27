package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        value = "product update에 대한 request",
        description = "수정이 필요한 항목만 사용하세요. 모두를 채울 필요는 없습니다"
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
    private Integer price;
    @ApiModelProperty(
            value = "제품 수량",
            required = false
    )
    private Integer stock;
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
    public ProductUpdateRequestDto(Long itemId, String productName, Integer price, Integer stock, Integer rateDiscount, Long storePostId) {
        this.itemId = itemId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.storePostId = storePostId;
    }
}
