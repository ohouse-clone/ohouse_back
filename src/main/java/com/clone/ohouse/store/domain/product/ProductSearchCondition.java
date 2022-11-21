package com.clone.ohouse.store.domain.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCondition {
    private String productName;
    private Integer priceBegin;
    private Integer priceEnd;
    private Integer stock;
    private Long popularBegin;


}
