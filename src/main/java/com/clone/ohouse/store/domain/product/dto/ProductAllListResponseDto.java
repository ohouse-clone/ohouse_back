package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductAllListResponseDto {
    private final Long productId;
    private final Item item;
    private final String productName;
    private final Integer price;
    private final Integer stock;
    private final Integer rateDiscount;

    public ProductAllListResponseDto(Product entity) {
        this.productId = entity.getId();
        this.item = entity.getItem();
        this.productName = entity.getProductName();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.rateDiscount = entity.getRateDiscount();
    }
}
