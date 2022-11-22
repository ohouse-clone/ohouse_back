package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductUpdateRequestDto {
    private Long itemId;
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;
    private Long storePostId;

    @Builder
    public ProductUpdateRequestDto(Long itemId, String productName, Integer price, Integer stock, Integer rateDiscount, Long storePostId) {
        this.itemId = itemId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.storePostId = storePostId;
    }
}
