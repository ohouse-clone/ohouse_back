package com.clone.ohouse.community.domain.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@ApiModel(
        description = "Comment API(GET /community/api/v1/comment/bundle)에 대한 Response 객체 <br>" +
                "CommentResponseDto 객체를 필드로 포함한다."
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentListResponseDto {

    @ApiModelProperty(
            value = "총 개수"
    )
    private Long totalNum;

    @ApiModelProperty(
            value = "댓글 리스트, CommentResponseDto 객체의 리스트"
    )
    private List<CommentResponseDto> commentList;

}
