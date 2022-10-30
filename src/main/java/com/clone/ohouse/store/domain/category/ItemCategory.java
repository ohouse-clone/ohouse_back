package com.clone.ohouse.store.domain.category;

import com.clone.ohouse.store.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

    public ItemCategory(Category category, Item item) {
        this.category = category;
        this.item = item;
    }
}
