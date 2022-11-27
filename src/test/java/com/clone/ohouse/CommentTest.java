package com.clone.ohouse;

import com.clone.ohouse.community.entity.Comment;
import com.clone.ohouse.community.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class CommentTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void createComment() throws Exception{
        Comment comment = Comment.builder()
                .commentTitle("title1")
                .commentAuthor("user1")
                .commentContent("newcomment")
                .build();

        //저장
        Comment newComment = commentRepository.save(comment);
        //조회
        Comment result = commentRepository.findById(comment.getId()).get();
        Optional<Comment> findByAuthor = commentRepository.findByCommentAuthor(comment.getCommentAuthor());
        Optional<Comment> findByTitle = commentRepository.findByCommentTitle(comment.getCommentTitle());

        assertThat(findByAuthor.get().getId()).isEqualTo(comment.getId());
        assertThat(findByAuthor.get()).isEqualTo(comment);
        assertThat(findByTitle.get().getId()).isEqualTo(comment.getId());
        assertThat(findByTitle.get()).isEqualTo(comment);

    }

    @Test
    public void CRUD() throws Exception{

        Comment comment1 = Comment.builder()
                .commentAuthor("user1")
                .commentTitle("title1")
                .commentContent("comment1")
                .build();

        Comment comment2 =  Comment.builder()
                .commentAuthor("user2")
                .commentTitle("title2")
                .commentContent("comment2")
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        Comment findComment1 = commentRepository.findById(comment1.getId()).get();
        Comment findComment2 = commentRepository.findById(comment2.getId()).get();
        assertThat(findComment1).isEqualTo(comment1);
        assertThat(findComment2).isEqualTo(comment2);


        List<Comment> comments = commentRepository.findAll();
        long count = commentRepository.count();

        assertThat(comments.size()).isEqualTo(count);
        assertThat(comments.size()).isEqualTo(2);

        commentRepository.delete(comment1);
        commentRepository.delete(comment2);
        long deletedCount = commentRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }
}
