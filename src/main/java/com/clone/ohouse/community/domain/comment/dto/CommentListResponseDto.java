package com.clone.ohouse.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentListResponseDto {
    private Long totalNum;
    private List<CommentResponseDto> commentList;

}
