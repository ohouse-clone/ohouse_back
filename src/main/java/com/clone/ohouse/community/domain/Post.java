package com.clone.ohouse.community.domain;

import com.clone.ohouse.utility.auditingtime.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PostType postType;

    public Post(PostType postType) {
        this.postType = postType;
    }
}
