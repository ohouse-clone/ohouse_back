package com.clone.ohouse.store.domain.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.Setter;

import static com.clone.ohouse.store.domain.item.QItem.item;


public class ItemSearchCondition {
    public String itemName;
    public String modelName;
    public String brandName;

    public BooleanExpression eqItemName(){
        if(itemName == null) return null;

        return item.name.eq(itemName);
    }
    public BooleanExpression eqModelName(){
        if(modelName == null) return null;

        return item.modelName.eq(modelName);
    }
    public BooleanExpression eqBrandName(){
        if(brandName == null) return null;

        return item.brandName.eq(brandName);
    }
}
