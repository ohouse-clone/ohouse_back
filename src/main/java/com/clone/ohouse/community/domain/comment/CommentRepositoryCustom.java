package com.clone.ohouse.community.domain.comment;

import com.clone.ohouse.community.domain.comment.dto.CommentResponseDto;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByPostId(Long postId);
    Optional<Comment> findByCommentId(Long commentId);
}
