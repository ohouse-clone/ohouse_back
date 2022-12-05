package com.clone.ohouse.community.domain.cardcollections.dto;

import com.clone.ohouse.community.domain.cardcollections.Color;
import com.clone.ohouse.community.domain.cardcollections.HouseStyle;
import com.clone.ohouse.community.domain.cardcollections.HousingType;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "save request 또는 update request에 사용되는 dto입니다. <br>" +
                "save request는 모든 값을 채워야하지만 update request는 null을 허용하며 수정이 필요한 부분만을 값을 채워야 합니다."
)
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
