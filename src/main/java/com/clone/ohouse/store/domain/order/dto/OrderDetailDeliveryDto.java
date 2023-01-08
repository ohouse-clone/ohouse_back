package com.clone.ohouse.store.domain.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Order API(GET /order/api/v1/order/{orderId})에 대한 Response 의 Field<br>"
                + "주문 상세 조회의 배송지 데이터"
)
@NoArgsConstructor
@Getter
public class OrderDetailDeliveryDto {
    @ApiModelProperty(
            value = "보내는이 성함"
    )
    private String recipientName;

    @ApiModelProperty(
            value = "연락처"
    )
    private String phone;

    @ApiModelProperty(
            value = "우편 번호"
    )
    private String zipCode;

    @ApiModelProperty(
            value = "배송지 경로 1"
    )
    private String address1;

    @ApiModelProperty(
            value = "배송지 경로 2"
    )
    private String address2;

    @ApiModelProperty(
            value = "배송시 전할 말"
    )
    private String memo;

    public OrderDetailDeliveryDto(String recipientName, String phone, String zipCode, String address1, String address2, String memo) {
        this.recipientName = recipientName;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.memo = memo;
    }
}
