package com.clone.ohouse.community.domain.comment;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.community.domain.cardcollections.Post;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long id;

    @Column(nullable = false, length = 40)
    private String commentTitle;
    @Column(nullable = false)
    private String commentContent;
    @Column(nullable = false, length = 30)
    private String commentAuthor;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "point")
    private User user;

    @ManyToOne
    @JoinColumn(name = "title")
    private Post post;

    @Builder
    public Comment(String commentTitle, String commentContent, String commentAuthor, LocalDateTime createdDate) {
        this.commentTitle = commentTitle;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.createdDate = createdDate;
    }

}
