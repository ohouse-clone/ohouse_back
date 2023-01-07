package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderDetailDeliveryDto {
    private String recipientName;
    private String phone;
    private String zipCode;
    private String address1;
    private String address2;
    private String memo;

    public OrderDetailDeliveryDto(String recipientName, String phone, String zipCode, String address1, String address2, String memo) {
        this.recipientName = recipientName;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.memo = memo;
    }
}
