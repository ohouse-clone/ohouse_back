package com.clone.ohouse.store.domain.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.clone.ohouse.store.domain.product.QProduct.*;

@Getter
@Setter
public class ProductSearchCondition {
    private String productName;
    private Integer priceBegin;
    private Integer priceEnd;
    private Integer stockBegin;
    private Long popularBegin;

    @Builder
    public ProductSearchCondition(String productName, Integer priceBegin, Integer priceEnd, Integer stockBegin, Long popularBegin) {
        this.productName = productName;
        this.priceBegin = priceBegin;
        this.priceEnd = priceEnd;
        this.stockBegin = stockBegin;
        this.popularBegin = popularBegin;
    }

    public ProductSearchCondition() {
    }

    public BooleanExpression eqProductName(){
        if(productName == null) return  null;
        return product.productName.eq(productName);
    }

    public BooleanExpression goePriceBegin(){
        if(priceBegin == null) return null;
        return product.price.goe(priceBegin);
    }
    public BooleanExpression loePriceEnd(){
        if(priceEnd ==null) return null;
        return product.price.loe(priceEnd);
    }
    public BooleanExpression goeStock(){
        if(stockBegin == null) return null;
        return product.stock.goe(stockBegin);
    }
    public BooleanExpression goePopular(){
        if(popularBegin == null) return null;
        return product.popular.goe(popularBegin);
    }
}
