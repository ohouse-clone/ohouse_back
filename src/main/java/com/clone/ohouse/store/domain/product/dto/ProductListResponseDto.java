package com.clone.ohouse.store.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
