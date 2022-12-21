package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderedProductDto {
    private Long productId;
    private Long adjustedPrice;
    private Long amount;

    public OrderedProductDto(Long productId, Long adjustedPrice, Long amount) {
        this.productId = productId;
        this.adjustedPrice = adjustedPrice;
        this.amount = amount;
    }
}
