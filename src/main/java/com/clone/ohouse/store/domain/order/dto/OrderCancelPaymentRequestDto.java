package com.clone.ohouse.store.domain.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Order API(POST /order/api/v1/payment/{orderId}/card/cancel) 의 Request <br>"
                + "주문의 카드 결제 취소 요청"
)
@Getter
@NoArgsConstructor
public class OrderCancelPaymentRequestDto {

    @ApiModelProperty(
            value = "주문 취소 사유",
            required = true
    )
    private String cancelReason;

    public OrderCancelPaymentRequestDto(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
