package com.clone.ohouse.community.repository;

import com.clone.ohouse.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    public Optional<Comment> findByCommentAuthor(String commentAuthor);
    public Optional<Comment> findByCommentTitle(String commentTitle);
    public void deleteByCommentTitle(String commentTitle);
}
