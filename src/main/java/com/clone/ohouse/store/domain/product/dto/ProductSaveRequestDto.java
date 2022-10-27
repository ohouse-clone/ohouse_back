package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSaveRequestDto {
    private Item item;
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;

    @Builder
    public ProductSaveRequestDto(Item item, String productName, Integer price, Integer stock, Integer rateDiscount, String size, String color) {
        this.item = item;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
    }

    public Product toEntity() {
        return Product.builder()
                .item(item)
                .productName(productName)
                .price(price)
                .stock(stock)
                .rateDiscount(rateDiscount)
                .build();
    }
}
