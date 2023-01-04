package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.payment.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDto {
    private PaymentType paymentType;
    private Long storePostId;
    private String orderName;
    private List<OrderedProductDto> orderList;


    public OrderRequestDto(PaymentType paymentType, Long storePostId, String orderName, List<OrderedProductDto> orderList) {
        this.paymentType = paymentType;
        this.storePostId = storePostId;
        this.orderName = orderName;
        this.orderList = orderList;
    }
}
