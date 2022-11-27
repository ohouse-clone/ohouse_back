package com.clone.ohouse.store.domain.item.bed;

import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;

import static com.clone.ohouse.store.domain.item.bed.QBed.bed;

public class BedSearchCondition extends ItemSearchCondition {
    public BedSize[] bedSize = new BedSize[8];
    public BedColor[] bedColor = new BedColor[3];

    public BooleanExpression eqBedCondition() {
        BooleanExpression sizeExpr = eqBedConditionSize();
        BooleanExpression colorExpr = eqBedConditionColor();
        if(sizeExpr == null && colorExpr == null) return null;
        else if(sizeExpr != null && colorExpr != null) return sizeExpr.and(colorExpr);
        else if(sizeExpr == null) return colorExpr;
        else if(colorExpr == null) return sizeExpr;

        return null;

    }

    private BooleanExpression eqBedConditionSize() {
        int i = 0;
        for (i = 0; i < bedSize.length; ++i)
            if (bedSize[i] == null) {
                --i;
                break;
            }
        if(i < 0) return null;
        BooleanExpression expr = bed.size.eq(this.bedSize[0]);
        for (int c = 1; c <= i; c++)
            expr = expr.or(bed.size.eq(this.bedSize[c]));

        return expr;
    }

    private BooleanExpression eqBedConditionColor() {
        int i = 0;
        for(i = 0; i < bedColor.length; ++i) if(bedColor[i] == null) {
            --i;
            break;
        }
        if(i < 0) return null;
        BooleanExpression expr = bed.color.eq(this.bedColor[0]);
        for(int c = 1; c <= i; c++){
            expr = expr.or(bed.color.eq(this.bedColor[c]));
        }

        return expr;
    }
}
