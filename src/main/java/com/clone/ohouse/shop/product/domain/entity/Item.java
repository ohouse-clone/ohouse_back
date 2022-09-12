package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ItemCategoryCode categoryCode;
    @Column(length = 45, nullable = false)
    private String name;
    @Column(length = 50)
    private String modelName;
    @Column(length = 45)
    private String brandName;

    public Item(ItemCategoryCode categoryCode, String name, String modelName, String brandName) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
    }


    public void update(ItemCategoryCode categoryCode, String name, String modelName, String brandName){
        if(categoryCode != null) this.categoryCode = categoryCode;
        if(name != null) this.name = name;
        if(modelName != null) this.modelName = modelName;
        if(brandName != null) this.brandName = brandName;
    }
}