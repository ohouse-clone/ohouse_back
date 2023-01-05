package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderBundleViewDto {
    private Long totalNum;
    private List<OrderViewDto> orderViewDtoList = new ArrayList<>();

    public OrderBundleViewDto(Long totalNum, List<OrderViewDto> orderViewDtoList) {
        this.totalNum = totalNum;
        this.orderViewDtoList = orderViewDtoList;
    }
}
