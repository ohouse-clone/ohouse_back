package com.clone.ohouse.store.domain.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
        description = "Order API(GET /order/api/v1/orders)에 대한 Response <br>"
                + "주문 리스트 조회 결과"
)
@Getter
@NoArgsConstructor
public class OrderBundleViewDto {

    @ApiModelProperty(
            value = "총 개수"
    )
    private Long totalNum;

    @ApiModelProperty(
            value = "주문 리스트"
    )
    private List<OrderViewDto> orderViewDtoList = new ArrayList<>();

    public OrderBundleViewDto(Long totalNum, List<OrderViewDto> orderViewDtoList) {
        this.totalNum = totalNum;
        this.orderViewDtoList = orderViewDtoList;
    }
}
