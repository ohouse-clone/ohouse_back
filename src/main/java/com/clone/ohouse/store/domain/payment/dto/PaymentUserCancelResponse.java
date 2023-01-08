package com.clone.ohouse.store.domain.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;


@ApiModel(
        description = "Order API(POST /order/api/v1/payment/{orderId}/card/cancel) 의 Response <br>"
                + "주문의 카드 결제 취소 결과"
)
@Getter
@NoArgsConstructor
public class PaymentUserCancelResponse {

    @ApiModelProperty(
            value = "카드 결제 취소 시각"
    )
    private String approvedAt;

    public PaymentUserCancelResponse(String approvedAt) {
        this.approvedAt = approvedAt;
    }
}
