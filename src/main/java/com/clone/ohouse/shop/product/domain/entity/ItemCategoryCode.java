package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ItemCategoryCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String categoryDetail;

    private Integer category1;

    private Integer category2;

    private Integer category3;

    private Integer category4;

    @Builder
    public ItemCategoryCode(String categoryDetail, Integer category1, Integer category2, Integer category3, Integer category4) {
        this.categoryDetail = categoryDetail;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.category4 = category4;
    }

}
