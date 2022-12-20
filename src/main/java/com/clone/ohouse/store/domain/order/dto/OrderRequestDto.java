package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.payment.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private PaymentType paymentType;
    private String orderName;
    private List<OrderedProductDto> orderList;

    public OrderRequestDto(PaymentType paymentType, String orderName, List<OrderedProductDto> orderList) {
        this.paymentType = paymentType;
        this.orderName = orderName;
        this.orderList = orderList;
    }
}
