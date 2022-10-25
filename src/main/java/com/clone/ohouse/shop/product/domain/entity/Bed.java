package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class Bed extends Item {
    @Column(length = 30)
    private String size;
    @Column(length = 30)
    private String color;


    @Builder
    public Bed(String name, String modelName, String brandName, String size, String color) {
        super(name, modelName, brandName);

        this.size = size;
        this.color = color;
    }

    public void update(String name, String modelName, String brandName, String size, String color) {
        super.update(name, modelName, brandName);
        if (size != null) this.size = size;
        if (color != null) this.color = color;
    }
}
