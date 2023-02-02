package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentUserFailResponseDto {
    private String code;
    private String message;
    private String orderId;

    public PaymentUserFailResponseDto(String code, String message, String orderId) {
        this.code = code;
        this.message = message;
        this.orderId = orderId;
    }
}
