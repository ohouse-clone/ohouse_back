package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetailResponseDto {
    private String orderId;
    private String orderStartDate;
    private Long totalPrice;

    private OrderDetailStorePostDto storePostDto;
    private OrderDetailDeliveryDto deliveryDto;

    public OrderDetailResponseDto(String orderId, String orderStartDate, Long totalPrice, OrderDetailStorePostDto storePostDto, OrderDetailDeliveryDto deliveryDto) {
        this.orderId = orderId;
        this.orderStartDate = orderStartDate;
        this.totalPrice = totalPrice;
        this.storePostDto = storePostDto;
        this.deliveryDto = deliveryDto;
    }
}
