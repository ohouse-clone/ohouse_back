package com.clone.ohouse.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CommentListResponseDto {
    private Long totalNum;
    private List<CommentResponseDto> commentList;

}
