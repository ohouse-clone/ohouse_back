package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentCancelRequestDto {
    private String cancelReason;

    public PaymentCancelRequestDto(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
