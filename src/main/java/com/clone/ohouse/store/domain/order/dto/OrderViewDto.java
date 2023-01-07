package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderViewDto {
    private String orderId;
    private LocalDateTime fixedTime;
    private OrderStatus status;
    private String postPreviewImageUrl;
    private String postTitle;
    private Long totalPrice;

    public OrderViewDto(String orderId, LocalDateTime fixedTime, OrderStatus status, String postPreviewImageUrl, String postTitle, Long totalPrice) {
        this.orderId = orderId;
        this.fixedTime = fixedTime;
        this.status = status;
        this.postPreviewImageUrl = postPreviewImageUrl;
        this.postTitle = postTitle;
        this.totalPrice = totalPrice;
    }
}
