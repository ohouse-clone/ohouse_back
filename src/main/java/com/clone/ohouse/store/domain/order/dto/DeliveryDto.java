package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.Delivery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;


@ApiModel(
        description = "Order API(POST /order/api/v1/order)에 대한 Request의 필드 <br>"
                + "배송 정보"
)
@NoArgsConstructor
@Getter
public class DeliveryDto {

    @ApiModelProperty(
            value = "보내는이 성함",
            required = true
    )
    private String senderName;

    @ApiModelProperty(
            value = "받는이 성함",
            required = true
    )
    private String recipientName;

    @ApiModelProperty(
            value = "우편 번호",
            required = true
    )
    private String zipCode;

    @ApiModelProperty(
            value = "배송지 이름",
            required = true
    )
    private String addressName;

    @ApiModelProperty(
            value = "배송지 경로 1",
            required = true
    )
    private String address1;
    @ApiModelProperty(
            value = "배송지 경로 2",
            required = true
    )
    private String address2;

    @ApiModelProperty(
            value = "연락처",
            required = true
    )
    private String phone;

    @ApiModelProperty(
            value = "배송시 전할 말",
            required = true
    )
    private String memo;

    public DeliveryDto(String senderName, String recipientName, String zipCode, String addressName, String address1, String address2, String phone, String memo) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.zipCode = zipCode;
        this.addressName = addressName;
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
        this.memo = memo;
    }

    public Delivery toEntity(){
        return Delivery.builder()
                .senderName(senderName)
                .recipientName(recipientName)
                .addressName(addressName)
                .zipCode(zipCode)
                .address1(address1)
                .address2(address2)
                .phone(phone)
                .memo(memo)
                .build();
    }
}
