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
        if(entity.getItem() != null) itemId = entity.getItem().getId();
        productName = entity.getProductName();
        price = entity.getPrice();
        stock = entity.getStock();
        rateDiscount = entity.getRateDiscount();
        popular = entity.getPopular();
        if(entity.getStorePosts() != null) storePostId = entity.getStorePosts().getId();
    }
}
