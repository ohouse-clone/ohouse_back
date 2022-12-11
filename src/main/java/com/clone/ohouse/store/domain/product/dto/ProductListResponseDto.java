package com.clone.ohouse.store.domain.product.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(
        description = "Product API(GET /store/api/v1/product/products)에 대한 응답 <br>" +
                "특정 item 하위의 products 조회 <br>" +
                "주로 반환에서 사용되며 product의 개수와 그 내용을 알 수 있습니다"
)
@NoArgsConstructor
@Getter
public class ProductListResponseDto {
    private Long totalNum;
    private Long productNum;
    private List<ProductResponseDto> products;

    public ProductListResponseDto(Long totalNum, Long productNum, List<ProductResponseDto> products) {
        this.totalNum = totalNum;
        this.productNum = productNum;
        this.products = products;
    }
}
