package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.payment.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@ApiModel(
        description = "Order API(POST /order/api/v1/order)에 대한 요청의 필드 <br>"
                + "주문 생성 요청에 필요한 정보"
)
@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDto {

    @ApiModelProperty(
            value = "결제 방식, 현재는 CARD만 사용 가능",
            required = true
    )
    private PaymentType paymentType;

    @ApiModelProperty(
            value = "StorePosts의 id",
            required = true
    )
    private Long storePostId;
    @ApiModelProperty(
            value = "간략한 주문 이름",
            required = true
    )
    private String orderName;

    @ApiModelProperty(
            value = "주문할 Product id가 담긴 리스트",
            required = true
    )
    private List<OrderedProductDto> orderList;


    public OrderRequestDto(PaymentType paymentType, Long storePostId, String orderName, List<OrderedProductDto> orderList) {
        this.paymentType = paymentType;
        this.storePostId = storePostId;
        this.orderName = orderName;
        this.orderList = orderList;
    }
}
