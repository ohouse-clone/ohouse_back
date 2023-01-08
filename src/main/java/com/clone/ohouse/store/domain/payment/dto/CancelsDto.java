package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelsDto {
    private Long cancelAmount;
    private String cancelReason;
    private Long taxFreeAmount;
    private Long taxExemptionAmount;
    private Long refundableAmount;
    private Long easyPayDiscountAmount;
    private String canceledAt;
    private String transactionKey;
}
