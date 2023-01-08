package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderSetStatusRequestDto {
    private OrderStatus status;

    public OrderSetStatusRequestDto(OrderStatus status) {
        this.status = status;
    }
}
