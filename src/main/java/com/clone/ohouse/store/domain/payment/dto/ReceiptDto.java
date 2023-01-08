package com.clone.ohouse.store.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceiptDto {
    private String url;

    public ReceiptDto(String url) {
        this.url = url;
    }
}
