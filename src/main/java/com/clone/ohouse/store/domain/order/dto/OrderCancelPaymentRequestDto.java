package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelPaymentRequestDto {
    private String cancelReason;

    public OrderCancelPaymentRequestDto(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
