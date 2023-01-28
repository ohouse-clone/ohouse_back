package com.clone.ohouse.store.domain.item.digital;

import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

import static com.clone.ohouse.store.domain.item.digital.QWashingMachine.washingMachine;

public class WashingMachineSearchCondition extends ItemSearchCondition {
    public List<RecommendNumber> recommendNumbers = new ArrayList<>();

    public BooleanBuilder eqWashingMachineCondition(){
        BooleanBuilder builder = new BooleanBuilder();

        builder
                .or(eqRecommendNumbers());

        return builder;
    }

    private BooleanExpression eqRecommendNumbers(){
        BooleanExpression expr = null;

        if(recommendNumbers.size() > 0) expr = washingMachine.recommendNumber.eq(recommendNumbers.get(0));
        for(int i = 1; i < recommendNumbers.size(); ++i){
            expr = expr.or(washingMachine.recommendNumber.eq(recommendNumbers.get(i)));
        }

        return expr;
    }

}
