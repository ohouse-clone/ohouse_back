package com.clone.ohouse.community.entity;

import com.clone.ohouse.community.dto.CommentDto;
import com.clone.ohouse.community.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Entity
@Table(name="post")
public class Post {
    @Id
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false,length = 30)
    private String author;
    @Column(name="modified_user",nullable = false,length = 30)
    private String modifiedUser;

    @Column(columnDefinition = "integer default 0")
    private Integer hit;

    private Boolean isActive;
    private Boolean isDelete;

    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    @Builder
    public Post(String title, String content, String author, String modifiedUser,Integer hit,Boolean isActive, Boolean isDelete
    ,LocalDateTime createDate, LocalDateTime modifiedDate){
        this.title = title;
        this.content = content;
        this.author = author;
        this.modifiedUser = modifiedUser;
        this.hit = hit;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "commentTitle", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comment> comments = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
