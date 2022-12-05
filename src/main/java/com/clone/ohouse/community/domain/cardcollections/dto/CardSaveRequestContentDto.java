package com.clone.ohouse.community.domain.cardcollections.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "save request 또는 update request에 사용되는 dto입니다. <br>" +
                "save request는 모든 값을 채워야하지만 update request는 null을 허용하며 수정이 필요한 부분만을 값을 채워야 합니다."
)
@NoArgsConstructor
@Getter
public class CardSaveRequestContentDto {
    @ApiModelProperty(
            name = "index",
            value = "사진 배열(files)와 연관되는 순서 번호",
            required = true
            )
    private Integer sequence;
    @ApiModelProperty(
            name = "content",
            value = "사진에 해당하는 content",
            required = true
    )
    private String content;

    public CardSaveRequestContentDto(Integer sequence, String content) {
        this.sequence = sequence;
        this.content = content;
    }

}
