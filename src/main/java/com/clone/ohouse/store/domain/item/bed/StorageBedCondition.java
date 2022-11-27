package com.clone.ohouse.store.domain.item.bed;

import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;

import static com.clone.ohouse.store.domain.item.bed.QStorageBed.*;

public class StorageBedCondition extends ItemSearchCondition {
    public Material[] material = new Material[4];

    public BooleanExpression eqStorageBedCondition(){
        BooleanExpression materialExpr = eqMaterials();
        if(materialExpr == null) return null;
        else if(materialExpr != null) return materialExpr;

        return null;
    }

    private BooleanExpression eqMaterials(){
        int i = 0;
        for(i = 0; i <material.length; ++i) if(material[i] == null){
            --i;
            break;
        }
        if(i<0) return null;

        BooleanExpression expr = storageBed.material.eq(this.material[0]);
        for(int c = 1; c <= i; c++) expr = expr.or(storageBed.material.eq(this.material[c]));

        return expr;
    }
}
