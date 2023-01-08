package com.clone.ohouse.store.domain.order.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Order API(GET /order/api/v1/order/{orderId})에 대한 Response 의 Field<br>"
                + "주문 상세 조회의 StorePost 데이터"
)
@NoArgsConstructor
@Getter
public class OrderDetailStorePostDto {
    private String postPreviewImageUrl;
    private String postTitle;

    public OrderDetailStorePostDto(String postPreviewImageUrl, String postTitle) {
        this.postPreviewImageUrl = postPreviewImageUrl;
        this.postTitle = postTitle;
    }
}
