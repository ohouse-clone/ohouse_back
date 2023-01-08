package com.clone.ohouse.store.domain.order.dto;

import com.clone.ohouse.store.domain.order.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@ApiModel(
        description = "Order API(GET /order/api/v1/orders)에 대한 Response 의 Field <br>"
                + "주문 리스트 조회의 item"
)
@Getter
@NoArgsConstructor
public class OrderViewDto {
    @ApiModelProperty(
            value = "주문 id"
    )
    private String orderId;

    @ApiModelProperty(
            value = "주문이 완료된 시간"
    )
    private LocalDateTime fixedTime;

    @ApiModelProperty(
            value = "주문의 상태"
    )
    private OrderStatus status;

    @ApiModelProperty(
            value = "주문한 StorePost 의 Preview Image Url"
    )
    private String postPreviewImageUrl;

    @ApiModelProperty(
            value = "주문한 StorePost 의 Title"
    )
    private String postTitle;

    @ApiModelProperty(
            value = "주문한 총 금액"
    )
    private Long totalPrice;

    public OrderViewDto(String orderId, LocalDateTime fixedTime, OrderStatus status, String postPreviewImageUrl, String postTitle, Long totalPrice) {
        this.orderId = orderId;
        this.fixedTime = fixedTime;
        this.status = status;
        this.postPreviewImageUrl = postPreviewImageUrl;
        this.postTitle = postTitle;
        this.totalPrice = totalPrice;
    }
}
