package com.clone.ohouse.shop.product.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "item_category_code",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "categoryUnique",
                        columnNames= {"category1", "category2", "category3", "category4"}
                )
        }
)
public class ItemCategoryCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String categoryDetail;

    @Column(name = "category1", nullable = false)
    private Integer category1;

    @Column(name = "category2", nullable = false)
    private Integer category2;

    @Column(name = "category3", nullable = false)
    private Integer category3;

    @Column(name = "category4", nullable = false)
    private Integer category4;


    @Builder
    public ItemCategoryCode(@NotNull String categoryDetail, @NotNull Integer category1, @NotNull Integer category2, @NotNull Integer category3, @NotNull Integer category4) {
        this.categoryDetail = categoryDetail;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.category4 = category4;
    }

}
