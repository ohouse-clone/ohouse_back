package com.clone.ohouse.community.domain.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@ApiModel(
        description = "Comment API(GET /community/api/v1/comment/{commentId})에 대한 Response 객체 <br>" +
                "또는 CommentListResponseDto 필드로 사용되는 객체"
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentResponseDto {

    @ApiModelProperty(
            value = "댓글 id"
    )
    private Long id;

    @ApiModelProperty(
            value = "게시글 id"
    )
    private Long postId;

    @ApiModelProperty(
            value = "댓글 내용"
    )
    private String content;

    @ApiModelProperty(
            value = "댓글 생성 시간"
    )
    private String createTime;

    @ApiModelProperty(
            value = "댓글을 남긴 닉네임"
    )
    private String userNickName;

    @ApiModelProperty(
            value = "좋아요 수"
    )
    private Long likeNumber;
}
