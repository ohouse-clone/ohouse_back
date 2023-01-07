package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetailResponseDto {
    private String orderId;
    private String orderStartDate;
    private Long totalPrice;

    private OrderStatus orderStatus;

    private OrderDetailStorePostDto storePostDto;
    private OrderDetailDeliveryDto deliveryDto;

    public OrderDetailResponseDto(String orderId, String orderStartDate, Long totalPrice, OrderStatus orderStatus,OrderDetailStorePostDto storePostDto, OrderDetailDeliveryDto deliveryDto) {
        this.orderId = orderId;
        this.orderStartDate = orderStartDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.storePostDto = storePostDto;
        this.deliveryDto = deliveryDto;
    }
}
