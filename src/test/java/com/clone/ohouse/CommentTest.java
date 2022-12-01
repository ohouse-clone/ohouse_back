package com.clone.ohouse;

import com.clone.ohouse.community.entity.Comment;
import com.clone.ohouse.community.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CommentTest {
    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    void clean(){
        commentRepository.deleteAll();
    }

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
        Comment result = commentRepository.findById(newComment.getId()).get();
        Optional<Comment> findByAuthor = commentRepository.findByCommentAuthor(comment.getCommentAuthor());
        Optional<Comment> findByTitle = commentRepository.findByCommentTitle(comment.getCommentTitle());

        assertThat(findByAuthor.get().getId()).isEqualTo(comment.getId());
        assertThat(findByAuthor.get().getCommentTitle()).isEqualTo(comment.getCommentTitle());
        assertThat(findByTitle.get().getId()).isEqualTo(comment.getId());
        assertThat(findByTitle.get().getCommentTitle()).isEqualTo(comment.getCommentTitle());

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
        assertThat(findComment1.getCommentTitle()).isEqualTo(comment1.getCommentTitle());
        assertThat(findComment2.getCommentContent()).isEqualTo(comment2.getCommentContent());


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
