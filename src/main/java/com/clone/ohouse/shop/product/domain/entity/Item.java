package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ItemCategoryCode categoryCode;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 50)
    private String modelName;

    @Column(length = 45)
    private String brandName;


    @Builder
    public Item(ItemCategoryCode categoryCode, String name, String modelName, String brandName) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
    }

    public void update(String name, String modelName, String brandName){
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
    }


    public void setName(String name) {
        this.name = name;
    }
}
