package com.clone.ohouse.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private String content;
    private String createTime;
    private String userNickName;
    private Long likeNumber;
}
