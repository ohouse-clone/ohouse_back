package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PaymentCompleteResponseDto {
    private String mId;
    private String version;
    private String paymentKey;
    private String status;
    private String lastTransactionKey;
    private String orderId;
    private String orderName;
    private String requestedAt;
    private String approvedAt;
    private boolean useEscrow;
    private boolean cultureExpense;

    private CardDto card;

    private String virtualAccount;
    private String transfer;
    private String mobilePhone;
    private String giftCertificate;
    private String cashReceipt;
    private String discount;
    private List<CancelsDto> cancels;
    private String secret;
    private String type;
    private String easyPay;
    private String country;
    private String failure;
    private String isPartialCancelable;

    private ReceiptDto receiptDto;
    private CheckoutDto checkoutDto;
    private String currency;
    private Long totalAmount;
    private Long balanceAmount;
    private Long suppliedAmount;
    private Long vat;
    private Long taxFreeAmount;
    private Long taxExemptionAmount;
    private String method;


}
