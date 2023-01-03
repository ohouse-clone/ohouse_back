package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class OrderResponse {

    private Long totalPrice;
    private String name;
    private String orderId;
    private LocalDateTime createTime;
    private String successUrl;
    private String failUrl;

    private String nextRequestUrl;

    public OrderResponse(Long totalPrice, String name, String orderId, LocalDateTime createTime, String successUrl, String failUrl, String nextRequestUrl) {
        this.totalPrice = totalPrice;
        this.name = name;
        this.orderId = orderId;
        this.createTime = createTime;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
        this.nextRequestUrl = nextRequestUrl;
    }
}
