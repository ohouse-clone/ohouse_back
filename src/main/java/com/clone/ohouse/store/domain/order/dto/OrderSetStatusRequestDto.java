package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Order API(POST /order/api/v1/{orderId}/status)에 대한 Request <br>"
                + "주문 상태 변경 요청"
)
@NoArgsConstructor
@Getter
public class OrderSetStatusRequestDto {
    @ApiModelProperty(
            value = "변경할 주문 상태",
            required = true
    )
    private OrderStatus status;

    public OrderSetStatusRequestDto(OrderStatus status) {
        this.status = status;
    }
}
