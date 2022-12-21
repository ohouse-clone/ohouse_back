package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.Delivery;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DeliveryDto {
    private String senderName;
    private String recipientName;
    private String zipCode;
    private String addressName;
    private String address1;
    private String address2;
    private String phone;
    private String memo;

    public DeliveryDto(String senderName, String recipientName, String zipCode, String addressName, String address1, String address2, String phone, String memo) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.zipCode = zipCode;
        this.addressName = addressName;
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
        this.memo = memo;
    }

    public Delivery toEntity(){
        return Delivery.builder()
                .senderName(senderName)
                .recipientName(recipientName)
                .addressName(addressName)
                .zipCode(zipCode)
                .address1(address1)
                .address2(address2)
                .phone(phone)
                .memo(memo)
                .build();
    }
}
