package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductSaveRequestDto {
    private Long itemId;
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;

    public ProductSaveRequestDto(Long itemId, String productName, Integer price, Integer stock, Integer rateDiscount) {
        this.itemId = itemId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
    }


}
