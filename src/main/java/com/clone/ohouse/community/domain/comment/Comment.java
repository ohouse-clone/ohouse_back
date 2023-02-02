package com.clone.ohouse.community.domain.comment;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.community.domain.Post;
import com.clone.ohouse.community.domain.PostType;
import com.clone.ohouse.community.domain.cardcollections.Card;
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

    @Column(nullable = false, length = 180)
    private String content;
    @Column
    private Long likeNumber = 0L;

    @Column (nullable = false)
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.createTime = LocalDateTime.now();
    }

    public void like(Long number){
        likeNumber += number;
    }
}
