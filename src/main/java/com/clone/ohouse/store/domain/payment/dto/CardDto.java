package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardDto {
    private String issuerCode;
    private String acquirerCode;
    private String number;
    private Integer installmentPlanMonths;
    private boolean isInterestFree;
    private String interestPayer;
    private String approveNo;
    private boolean useCardPoint;
    private String cardType;
    private String ownerType;
    private String acquireStatus;
    private String receiptUrl;
    private Long amount;
}
