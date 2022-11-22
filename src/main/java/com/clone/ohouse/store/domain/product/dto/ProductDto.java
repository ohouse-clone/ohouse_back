package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductDto {
    private Long id;
    private Long itemId;
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;
    private Long popular;
    private Long storePostId;

    public ProductDto(Product entity){
        id = entity.getId();
        itemId = entity.getItem().getId();
        productName = entity.getProductName();
        price = entity.getPrice();
        stock = entity.getStock();
        rateDiscount = entity.getRateDiscount();
        popular = entity.getPopular();
        storePostId = entity.getStorePosts().getId();
    }
}
