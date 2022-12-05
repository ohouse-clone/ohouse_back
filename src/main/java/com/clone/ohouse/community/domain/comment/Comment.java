package com.clone.ohouse.community.domain.comment;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.community.domain.Post;
import com.clone.ohouse.community.domain.PostType;
import com.clone.ohouse.community.domain.cardcollections.Card;
import lombok.*;


import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long id;

    @Column(nullable = false, length = 180)
    private String content;
    @Column
    private boolean isDeleted = false;

    @Column
    private Long likeNumber = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content, Long likeNumber, User user, Post post) {
        this.content = content;
        this.likeNumber = likeNumber;
        this.user = user;
        this.post = post;
    }
}
