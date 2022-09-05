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

    public Item(ItemCategoryCode categoryCode, String name) {
        this.categoryCode = categoryCode;
        this.name = name;
    }

    public void update(ItemCategoryCode categoryCode, String name){
        if(categoryCode != null) this.categoryCode = categoryCode;
        if(name != null) this.name = name;
    }
}