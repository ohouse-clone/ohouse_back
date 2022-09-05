package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class Furniture extends Item {

    @Column(length = 50)
    private String modelName;
    @Column(length = 45)
    private String brandName;
    @Column(length = 30)
    private String size;
    @Column(length = 30)
    private String color;


    @Builder
    public Furniture(ItemCategoryCode categoryCode, String name, String modelName, String brandName, String size, String color) {
        super(categoryCode, name);
        this.modelName = modelName;
        this.brandName = brandName;
        this.size = size;
        this.color = color;
    }

    public void update(ItemCategoryCode categoryCode, String name, String modelName, String brandName, String size, String color) {
        super.update(categoryCode, name);
        if (modelName != null) this.modelName = modelName;
        if (brandName != null) this.brandName = brandName;
        if (size != null) this.size = size;
        if (color != null) this.color = color;
    }
}
