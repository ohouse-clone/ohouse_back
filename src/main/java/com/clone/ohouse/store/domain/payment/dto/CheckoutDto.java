package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckoutDto {
    private String url;

    public CheckoutDto(String url) {
        this.url = url;
    }
}
