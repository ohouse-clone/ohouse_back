package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

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
