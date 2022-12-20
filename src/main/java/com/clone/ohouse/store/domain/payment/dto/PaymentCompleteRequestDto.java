package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentCompleteRequestDto {
    private String paymentKey;
    private String orderId;
    private Long amount;

    public PaymentCompleteRequestDto(String paymentKey, String orderApprovalCode, Long amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderApprovalCode;
        this.amount = amount;
    }
}
