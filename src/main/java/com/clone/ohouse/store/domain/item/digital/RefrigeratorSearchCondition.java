package com.clone.ohouse.store.domain.item.digital;

import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

import static com.clone.ohouse.store.domain.item.digital.QRefrigerator.refrigerator;

public class RefrigeratorSearchCondition extends ItemSearchCondition {
    public List<RefrigeratorCapacity> refrigeratorCapacities = new ArrayList<>();

    public BooleanBuilder eqRefrigeratorCondition(){
        BooleanBuilder builder = new BooleanBuilder();

        builder
                .or(eqRefrigeratorCapacities());

        return builder;
    }

    private BooleanExpression eqRefrigeratorCapacities(){
        BooleanExpression expr = null;

        if(refrigeratorCapacities.size() > 0) expr = refrigerator.capacity.eq(refrigeratorCapacities.get(0));
        for(int i = 1; i < refrigeratorCapacities.size(); ++i){
            expr = expr.or(refrigerator.capacity.eq(refrigeratorCapacities.get(i)));
        }

        return expr;
    }

}
