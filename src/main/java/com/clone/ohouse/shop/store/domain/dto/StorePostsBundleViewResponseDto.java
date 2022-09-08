package com.clone.ohouse.shop.store.domain.dto;

import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StorePostsBundleViewResponseDto {
    private final Long id;
    private final String brandName;
    private final String title;
    private final Integer price;
    private final Integer rateDiscount;

    public StorePostsBundleViewResponseDto(StorePosts entity) {
        this.id = entity.getId();
        this.brandName = entity.getProductList().get(0).getItem().getBrandName();
        this.title = entity.getTitle();
        this.price = entity.getProductList().get(0).getPrice();
        this.rateDiscount = entity.getProductList().get(0).getRateDiscount();
    }
}
