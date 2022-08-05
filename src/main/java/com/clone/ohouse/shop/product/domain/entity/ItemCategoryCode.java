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
    private Long categorySeq;

    @Column(nullable = false, length = 50)
    private String categoryDetail;

    @Column(length = 4)
    private String category1;

    @Column(length = 4)
    private String category2;

    @Column( length = 4)
    private String category3;

    @Column(length = 4)
    private String category4;

    @Builder
    public ItemCategoryCode(String categoryDetail, String category1, String category2, String category3, String category4) {
        this.categoryDetail = categoryDetail;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.category4 = category4;
    }

}
