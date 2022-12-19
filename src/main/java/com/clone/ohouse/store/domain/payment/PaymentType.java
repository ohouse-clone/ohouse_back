package com.clone.ohouse.store.domain.payment;

public enum PaymentType {
    CARD("CARD");

    private String name;

    PaymentType(String name) {
        this.name = name;
    }
}
