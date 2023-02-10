package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedProductDto {
    private Long productId;
    private Integer adjustedPrice;
    private Integer amount;
}
