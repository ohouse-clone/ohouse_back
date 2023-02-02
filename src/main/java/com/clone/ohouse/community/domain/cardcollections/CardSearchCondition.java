package com.clone.ohouse.community.domain.cardcollections;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;

@Getter
public class CardSearchCondition {
    private HousingType housingType;
    private HouseStyle houseStyle;
    private Color color;

    private SortOrder sortOrder;

    public CardSearchCondition(HousingType housingType, HouseStyle houseStyle, Color color, SortOrder sortOrder) {
        this.housingType = housingType;
        this.houseStyle = houseStyle;
        this.color = color;
        this.sortOrder = sortOrder;
    }

    public BooleanExpression eqHosingType(){
        if(housingType == null) return null;

        return QCard.card.housingType.eq(housingType);
    }
    public BooleanExpression eqHouseStyle(){
        if(houseStyle == null) return null;

        return QCard.card.houseStyle.eq(houseStyle);
    }
    public BooleanExpression eqColor(){
        if(color == null) return null;
        return QCard.card.color.eq(color);
    }

    public OrderSpecifier<Long> orderByHit(){
        if(sortOrder == null) return QCard.card.hit.desc();

        switch (sortOrder){
            case HIT_ASCEND:
                return QCard.card.hit.asc();
            case HIT_DESCEND:
                return QCard.card.hit.desc();
            default:
                return null;
        }
    }

}
