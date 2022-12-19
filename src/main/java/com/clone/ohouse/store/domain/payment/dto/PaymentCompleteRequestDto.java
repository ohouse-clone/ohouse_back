package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentCompleteRequestDto {
    private String encodedAuth;
    private String orderApprovalCode;
    private Long amount;

    public PaymentCompleteRequestDto(String encodedAuth, String orderApprovalCode, Long amount) {
        this.encodedAuth = encodedAuth;
        this.orderApprovalCode = orderApprovalCode;
        this.amount = amount;
    }
}
