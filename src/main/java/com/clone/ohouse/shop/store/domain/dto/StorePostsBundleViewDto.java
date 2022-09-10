package com.clone.ohouse.shop.store.domain.dto;

import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import lombok.Getter;

@Getter
public class StorePostsBundleViewDto {
    private final Long id;
    private final String brandName;
    private final String title;
    private final Integer price;
    private final Integer rateDiscount;
    private final byte[] previewImage;

    public StorePostsBundleViewDto(StorePosts entity) {
        this.id = entity.getId();
        this.brandName = entity.getProductList().get(0).getItem().getBrandName();
        this.title = entity.getTitle();
        this.price = entity.getProductList().get(0).getPrice();
        this.rateDiscount = entity.getProductList().get(0).getRateDiscount();
        this.previewImage = entity.getPreviewImage();
    }
}
