package com.clone.ohouse.community.domain.cardcollections.dto;

import com.clone.ohouse.community.domain.cardcollections.Color;
import com.clone.ohouse.community.domain.cardcollections.HouseStyle;
import com.clone.ohouse.community.domain.cardcollections.HousingType;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@ApiModel(
        description = "Card를 상세 조회한 결과입니다."
)
@NoArgsConstructor
@Getter
public class CardResponseDto {
    private Long id;
    private Long hit;
    private HousingType housingType;
    private HouseStyle houseStyle;
    private Color color;

    private Integer contentNum = 0;
    private List<CardContentResponseDto> contentList = new ArrayList<>();
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    public CardResponseDto(Long id, Long hit, HousingType housingType, HouseStyle houseStyle, Color color, LocalDateTime createDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.hit = hit;
        this.housingType = housingType;
        this.houseStyle = houseStyle;
        this.color = color;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }

    public void setContentList(List<CardContentResponseDto> contentList) {
        this.contentNum = contentList.size();
        this.contentList = contentList;
    }
}
