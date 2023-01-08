package com.clone.ohouse.store.domain.order.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@ApiModel(
        description = "Order API(POST /order/api/v1/order)에 대한 Request <br>"
                + "주문 생성 요청"
)
@NoArgsConstructor
@Getter
public class StartOrderRequestDto {
    private OrderRequestDto orderRequestDto;
    private DeliveryDto deliveryDto;

    public StartOrderRequestDto(OrderRequestDto orderRequestDto, DeliveryDto deliveryDto) {
        this.orderRequestDto = orderRequestDto;
        this.deliveryDto = deliveryDto;
    }
}
