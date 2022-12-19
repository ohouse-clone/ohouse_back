package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class OrderResponse {

    private Integer totalPrice;
    private String name;
    private String paymentApprovalCode;
    private LocalDateTime createTime;
    private String successUrl;
    private String failUrl;

    public OrderResponse(Integer totalPrice, String name, String paymentApprovalCode, LocalDateTime createTime, String successUrl, String failUrl) {
        this.totalPrice = totalPrice;
        this.name = name;
        this.paymentApprovalCode = paymentApprovalCode;
        this.createTime = createTime;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
    }
}
