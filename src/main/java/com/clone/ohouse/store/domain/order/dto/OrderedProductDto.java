package com.clone.ohouse.store.domain.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(
        description = "Order API(POST /order/api/v1/order) Request 요청에 필요한 field <br>"
                + "주문하게 될 제품의 정보"
)
@NoArgsConstructor
@Getter
@Setter
public class OrderedProductDto {

    @ApiModelProperty(
            value = "Product id",
            required = true
    )
    private Long productId;
    @ApiModelProperty(
            value = "조정된 구매 금액",
            required = true
    )
    private Long adjustedPrice;

    @ApiModelProperty(
            value = "구매 금액",
            required = true
    )
    private Long amount;

    public OrderedProductDto(Long productId, Long adjustedPrice, Long amount) {
        this.productId = productId;
        this.adjustedPrice = adjustedPrice;
        this.amount = amount;
    }
}
