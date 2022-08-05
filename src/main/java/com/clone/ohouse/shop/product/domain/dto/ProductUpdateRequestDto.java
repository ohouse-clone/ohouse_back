package com.clone.ohouse.shop.product.domain.dto;

import com.clone.ohouse.shop.product.domain.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductUpdateRequestDto {
    private Item item;
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;
    private String size;
    private String color;

    private Character optionalYn;

    @Builder
    public ProductUpdateRequestDto(Item item, String productName, Integer price, Integer stock, Integer rateDiscount, String size, String color, Character optionalYn) {
        this.item = item;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.size = size;
        this.color = color;
        this.optionalYn = optionalYn;
    }
}
