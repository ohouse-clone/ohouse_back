package com.clone.ohouse.community.domain.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Comment API(POST /community/api/v1/comment)에 대한 Request 객체 <br>"
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentSaveDto {

    @ApiModelProperty(
            value = "게시글의 id",
            required = true
    )
    private Long postId;

    @ApiModelProperty(
            value = "댓글 내용",
            required = true
    )
    private String content;
}
