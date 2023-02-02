package com.clone.ohouse.store.domain.item.table;


import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

import static com.clone.ohouse.store.domain.item.table.QDesk.desk;

public class DeskSearchCondition extends ItemSearchCondition {
    public List<DeskColor> deskColors = new ArrayList<>();
    public List<FrameMaterial> frameMaterials = new ArrayList<>();
    public List<UsageType> usageTypes = new ArrayList<>();

    public BooleanBuilder eqDeskCondition() {
        BooleanBuilder builder = new BooleanBuilder();

        builder
                .or(eqDeskColors())
                .or(eqFrameMaterials())
                .or(eqUsageTypes());
        return builder;
    }
    private BooleanExpression eqDeskColors(){
        BooleanExpression expr = null;

        if(deskColors.size() > 0) expr = desk.color.eq(deskColors.get(0));
        for(int i = 1; i < deskColors.size(); ++i){
            expr = expr.or(desk.color.eq(deskColors.get(i)));
        }
        return expr;
    }
    private BooleanExpression eqFrameMaterials(){
        BooleanExpression expr = null;

        if(frameMaterials.size() > 0) expr = desk.frameMaterial.eq(frameMaterials.get(0));
        for(int i = 1; i < frameMaterials.size(); ++i){
            expr = expr.or(desk.frameMaterial.eq(frameMaterials.get(i)));
        }
        return expr;
    }

    private BooleanExpression eqUsageTypes(){
        BooleanExpression expr = null;

        if(usageTypes.size() > 0) expr = desk.usageType.eq(usageTypes.get(0));
        for(int i = 1; i < usageTypes.size(); ++i){
            expr = expr.or(desk.usageType.eq(usageTypes.get(i)));
        }
        return expr;
    }
}

