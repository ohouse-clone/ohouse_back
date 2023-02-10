package com.clone.ohouse.community.entity;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long id;


    @Column(name = "comment_title", nullable = false, length = 40)
    private String commentTitle;
    @Column(name = "comment_content", nullable = false)
    private String commentContent;
    @Column(name = "comment_author", nullable = false, length = 30)
    private String commentAuthor;
    @Column
    private Long likeNumber = 0L;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @Builder
    public Comment(String commentTitle, String commentContent, String commentAuthor, LocalDateTime createTime) {
        this.commentTitle = commentTitle;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.createTime = createTime;
    }

    public void like(Long number) {
        likeNumber += number;
    }
}
