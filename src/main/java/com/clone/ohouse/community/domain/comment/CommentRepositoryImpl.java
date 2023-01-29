package com.clone.ohouse.community.domain.comment;

import com.clone.ohouse.community.domain.comment.dto.CommentResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.clone.ohouse.account.domain.user.QUser.user;
import static com.clone.ohouse.community.domain.QPost.post;
import static com.clone.ohouse.community.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findByUserId(Long userId) {
        List<Comment> fetch = queryFactory
                .select(comment)
                .from(comment)
                .join(comment.user, user).fetchJoin()
                .join(comment.post, post).fetchJoin()
                .where(comment.user.id.eq(userId))
                .fetch();

        return fetch;

    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        List<Comment> fetch = queryFactory
                .select(comment)
                .from(comment)
                .join(comment.user, user).fetchJoin()
                .join(comment.post, post).fetchJoin()
                .where(comment.post.id.eq(postId))
                .fetch();
        return fetch;
    }

    @Override
    public Optional<Comment> findByCommentId(Long commentId) {
        return Optional.ofNullable(queryFactory
                .select(comment)
                .from(comment)
                .join(comment.user, user).fetchJoin()
                .join(comment.post, post).fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne());
    }
}
