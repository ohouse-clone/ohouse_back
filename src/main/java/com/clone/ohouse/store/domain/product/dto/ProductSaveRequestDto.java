package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.product.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        value = "제품 등록에 대한 request"
)
@NoArgsConstructor
@Getter
public class ProductSaveRequestDto {
    @ApiModelProperty(
            value = "item id",
            required = true
    )
    private Long itemId;
    @ApiModelProperty(
            value = "제품 이름",
            required = true
    )
    private String productName;
    @ApiModelProperty(
            value = "제품 가격",
            required = true
    )
    private Integer price;
    @ApiModelProperty(
            value = "제품 재고",
            required = true
    )
    private Integer stock;

    @ApiModelProperty(
            value = "할인율",
            required = true
    )
    private Integer rateDiscount;

    @ApiModelProperty(
            value = "등록되있는 post id",
            required = false
    )
    private Long storePostId;

    @Builder
    public ProductSaveRequestDto(Long itemId, String productName, Integer price, Integer stock, Integer rateDiscount, Long storePostId) {
        this.itemId = itemId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.storePostId = storePostId;
    }


}
