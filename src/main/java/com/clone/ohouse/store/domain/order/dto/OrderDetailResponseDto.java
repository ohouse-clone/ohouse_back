package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Order API(GET /order/api/v1/order/{orderId})에 대한 Response <br>"
                + "주문 객체의 상세 정보"
)
@Getter
@NoArgsConstructor
public class OrderDetailResponseDto {

    @ApiModelProperty(
            value = "주문 id"
    )
    private String orderId;

    @ApiModelProperty(
            value = "주문이 만들어진 시간"
    )
    private String orderStartDate;

    @ApiModelProperty(
            value = "주문한 총 금액"
    )
    private Long totalPrice;

    @ApiModelProperty(
            value = "주문의 상태"
    )
    private OrderStatus orderStatus;

    @ApiModelProperty(
            value = "주문한 StorePost 의 정보"
    )
    private OrderDetailStorePostDto storePostDto;

    @ApiModelProperty(
            value = "주문한 배송지의 정보"
    )
    private OrderDetailDeliveryDto deliveryDto;

    public OrderDetailResponseDto(String orderId, String orderStartDate, Long totalPrice, OrderStatus orderStatus,OrderDetailStorePostDto storePostDto, OrderDetailDeliveryDto deliveryDto) {
        this.orderId = orderId;
        this.orderStartDate = orderStartDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.storePostDto = storePostDto;
        this.deliveryDto = deliveryDto;
    }
}
