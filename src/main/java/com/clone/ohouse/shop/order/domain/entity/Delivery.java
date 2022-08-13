package com.clone.ohouse.shop.order.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//Temperary Mockup Delivery class
@Getter
@NoArgsConstructor
@Entity
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long deliverySeq;

    @Column(length = 30) private String senderName;
    @Column(length = 30) private String receipentName;
    @Column(length = 5) private String zipCode;
    @Column(length = 50) private String addressName;
    @Column(length = 100) private String address1;
    @Column(length = 100) private String address2;
    @Column(length = 20) private String phone;
    @Column(length = 100) private String memo;

    public Delivery(String senderName, String receipentName, String zipCode, String addressName, String address1, String address2, String phone, String memo) {
        this.senderName = senderName;
        this.receipentName = receipentName;
        this.zipCode = zipCode;
        this.addressName = addressName;
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
        this.memo = memo;
    }
}
