package com.clone.ohouse.community.domain;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.cardcollections.Card;
import com.clone.ohouse.community.domain.cardcollections.CardRepository;
import com.clone.ohouse.community.domain.comment.Comment;
import com.clone.ohouse.community.domain.comment.CommentRepository;
import com.clone.ohouse.community.domain.comment.dto.CommentListResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentSaveDto;
import com.clone.ohouse.error.comment.CommentError;
import com.clone.ohouse.error.comment.CommentFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public Long save(SessionUser sessionUser, CommentSaveDto saveDto) throws Exception {
        //find user
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new CommentFailException("찾으려는 user 없음, email : " + sessionUser.getEmail(), CommentError.WRONG_USER_ID));
        //find post
        Card card = cardRepository.findById(saveDto.getPostId()).orElseThrow(() -> new CommentFailException("card id가 잘못됨 ; " + saveDto.getPostId(), CommentError.WRONG_POST_ID));

        return commentRepository.save(
                new Comment(
                        saveDto.getContent(),
                        user,
                        card)).getId();
    }

    public void delete(Long commentId) throws Exception{
        //find comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentFailException("찾으려는 comment 가 없음,comment id : " + commentId, CommentError.WRONG_COMMENT_ID));

        commentRepository.delete(comment);
    }

    public CommentResponseDto findByCommentId(Long commentId) throws Exception{
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new CommentFailException("찾으려는 comment 가 없음,comment id : " + commentId, CommentError.WRONG_COMMENT_ID));

        return new CommentResponseDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                comment.getUser().getNickname(),
                comment.getLikeNumber());
    }

    public CommentListResponseDto findBundleByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        ArrayList<CommentResponseDto> collect = comments.stream()
                .map((t) -> new CommentResponseDto(
                        t.getId(),
                        t.getPost().getId(),
                        t.getContent(),
                        t.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        t.getUser().getNickname(),
                        t.getLikeNumber()))
                .collect(Collectors.toCollection(ArrayList<CommentResponseDto>::new));

        return new CommentListResponseDto(Long.valueOf(collect.size()), collect);
    }
    public CommentListResponseDto findBundleByUserId(SessionUser sessionUser) throws Exception{
        //find user
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new CommentFailException("찾으려는 user 없음, email : " + sessionUser.getEmail(), CommentError.WRONG_USER_ID));

        List<Comment> comments = commentRepository.findByUserId(user.getId());

        ArrayList<CommentResponseDto> collect = comments.stream()
                .map((t) -> new CommentResponseDto(
                        t.getId(),
                        t.getPost().getId(),
                        t.getContent(),
                        t.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        t.getUser().getNickname(),
                        t.getLikeNumber()))
                .collect(Collectors.toCollection(ArrayList<CommentResponseDto>::new));
        return new CommentListResponseDto(Long.valueOf(collect.size()), collect);
    }

    public void actLike(Long commentId) throws Exception{
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentFailException("찾으려는 comment 가 없음,comment id : " + commentId, CommentError.WRONG_COMMENT_ID));
        
        comment.like(1L);
    }
}
