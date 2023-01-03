package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentUserCancelResponse {
    private String approvedAt;

    public PaymentUserCancelResponse(String approvedAt) {
        this.approvedAt = approvedAt;
    }
}
