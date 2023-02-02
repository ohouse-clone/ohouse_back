package com.clone.ohouse.store.domain.item.table;

import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.clone.ohouse.store.domain.item.table.QDiningTable.diningTable;

public class DiningTableSearchCondition extends ItemSearchCondition {
    public List<DiningTableShape> diningTableShapes;
    public List<FrameMaterial> frameMaterials;
    public List<NumberOfUsers> numberOfUsers;

    public BooleanBuilder eqDiningTableCondition() {
        BooleanBuilder builder = new BooleanBuilder();

        builder
                .or(eqDiningTableShapes())
                .or(eqFrameMaterials())
                .or(eqNumberOfUsages());

        return builder;
    }

    private BooleanExpression eqDiningTableShapes(){
        BooleanExpression expr = null;

        if(diningTableShapes.size() > 0) expr = diningTable.tableShape.eq(diningTableShapes.get(0));
        for(int i = 1; i < diningTableShapes.size(); ++i) {
            expr = expr.or(diningTable.tableShape.eq(diningTableShapes.get(i)));
        }

        return expr;
    }

    private BooleanExpression eqFrameMaterials(){
        BooleanExpression expr = null;

        if(frameMaterials.size() > 0) expr = diningTable.frameMaterial.eq(frameMaterials.get(0));
        for(int i = 1; i < frameMaterials.size(); ++i) {
            expr = expr.or(diningTable.frameMaterial.eq(frameMaterials.get(i)));
        }

        return expr;
    }
    private BooleanExpression eqNumberOfUsages(){
        BooleanExpression expr = null;

        if(numberOfUsers.size() > 0) expr = diningTable.numberOfUsers.eq(numberOfUsers.get(0));
        for(int i = 1; i < numberOfUsers.size(); ++i) {
            expr = expr.or(diningTable.numberOfUsers.eq(numberOfUsers.get(i)));
        }

        return expr;
    }
}
