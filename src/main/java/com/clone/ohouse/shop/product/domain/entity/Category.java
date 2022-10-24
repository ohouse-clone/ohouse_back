package com.clone.ohouse.shop.product.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public Category(String name, Long code) {
        this.name = name;
        this.code = code;
    }

    public void registerParent(Category parent){
        this.parent = parent;
        parent.getChild().add(this);
    }
}
