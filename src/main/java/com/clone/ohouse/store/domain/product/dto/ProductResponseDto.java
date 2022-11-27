package com.clone.ohouse.store.domain.product.dto;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.product.Product;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(
        value = "item 정보를 포함한 product 상세"
)
@Getter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;

    private Long itemId;
    private String itemName;
    private String modelName;
    private String brandName;

    public ProductResponseDto(Product entity) {
        this.id = entity.getId();
        this.productName = entity.getProductName();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.rateDiscount = entity.getRateDiscount();
        if(entity.getItem() != null) {
            Item item = entity.getItem();
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.modelName = item.getModelName();
            this.brandName = item.getBrandName();
        }
    }
}
