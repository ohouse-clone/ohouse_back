package com.clone.ohouse.store.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ProductStorePostIdUpdateRequestDto {
    private Long storePostId;
    private List<Long> productIds;

    public ProductStorePostIdUpdateRequestDto(Long storePostId, List<Long> productIds) {
        this.storePostId = storePostId;
        this.productIds = productIds;
    }
}
