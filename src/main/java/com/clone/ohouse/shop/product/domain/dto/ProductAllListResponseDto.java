package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.Product;
import lombok.Getter;

@Getter
public class ProductAllListResponseDto {
    private final Long productSeq;
    private final Item item;
    private final String productName;
    private final Integer price;
    private final Integer stock;
    private final Integer rateDiscount;
    private final String size;
    private final String color;

    public ProductAllListResponseDto(Product entity) {
        this.productSeq = entity.getProductSeq();
        this.item = entity.getItem();
        this.productName = entity.getProductName();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.rateDiscount = entity.getRateDiscount();
        this.size = entity.getSize();
        this.color = entity.getColor();
    }
}
