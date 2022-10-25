package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;


    @OneToMany(mappedBy = "item")
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @Column(length = 45, nullable = false)
    private String name;
    @Column(length = 50)
    private String modelName;
    @Column(length = 45)
    private String brandName;

    public Item(String name, String modelName, String brandName) {
        this.name = name;
        this.modelName = modelName;
        this.brandName = brandName;
    }


    public void update(String name, String modelName, String brandName){
        if(name != null) this.name = name;
        if(modelName != null) this.modelName = modelName;
        if(brandName != null) this.brandName = brandName;
    }
}