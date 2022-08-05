package com.clone.ohouse.shop.order.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private List<OrderedProductDto> orderList;
}
