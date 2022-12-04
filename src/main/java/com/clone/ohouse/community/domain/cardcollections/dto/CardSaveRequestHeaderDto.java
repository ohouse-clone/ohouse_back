package com.clone.ohouse.community.domain.cardcollections.dto;

import com.clone.ohouse.community.domain.cardcollections.Color;
import com.clone.ohouse.community.domain.cardcollections.HouseStyle;
import com.clone.ohouse.community.domain.cardcollections.HousingType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CardSaveRequestHeaderDto {
    private HousingType housingType;
    private HouseStyle houseStyle;
    private Color color;

    public CardSaveRequestHeaderDto(HousingType housingType, HouseStyle houseStyle, Color color) {
        this.housingType = housingType;
        this.houseStyle = houseStyle;
        this.color = color;
    }
}
