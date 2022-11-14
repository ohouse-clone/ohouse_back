package com.clone.ohouse.store.domain.category;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category {
    @Transient
    public static final Integer CATEGORY_SIZE = 4;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
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

    public void addParent(Category parent){
        this.parent = parent;
        if
        parent.getChild().add(this);
    }
    public void addChild(Category child){
        this.child.add(child);
        child.parent = this;
    }
}
