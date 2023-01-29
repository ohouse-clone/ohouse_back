package com.clone.ohouse.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentSaveDto {
    private Long postId;
    private String content;
}
