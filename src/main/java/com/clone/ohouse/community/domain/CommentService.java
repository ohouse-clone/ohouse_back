package com.clone.ohouse.community.domain;

import com.clone.ohouse.community.domain.comment.Comment;
import com.clone.ohouse.community.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional

public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        commentRepository.findAll().forEach(s -> comments.add(s));
        return comments;
    }

    public Optional<Comment> findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment;
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

//    public void updateComment(Long id) {
//        Optional<Comment> comment = commentRepository.findById(id);
//        comment.ifPresent(newComment -> {
//            comment.get().setCommentAuthor(newComment.getCommentAuthor());
//            comment.get().setCommentTitle(newComment.getCommentTitle());
//            comment.get().setCommentContent(newComment.getCommentContent());
//            comment.get().setId(newComment.getId());
//        });
//    }
}
