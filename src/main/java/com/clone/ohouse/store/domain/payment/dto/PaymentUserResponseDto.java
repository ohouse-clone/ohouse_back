package com.clone.ohouse.store.domain.payment.dto;

import com.clone.ohouse.store.domain.payment.PaymentResultStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentUserResponseDto {
    private String requestedAt;
    private String approvedAt;
    private PaymentResultStatus status;
    private Long totalAmount;
    private Long balanceAmount;

    public PaymentUserResponseDto(String requestedAt, String approvedAt, PaymentResultStatus status, Long totalAmount, Long balanceAmount) {
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.status = status;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
    }
}
