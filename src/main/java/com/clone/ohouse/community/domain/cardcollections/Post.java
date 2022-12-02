package com.clone.ohouse.community.domain.cardcollections;

import com.clone.ohouse.account.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Column(length = 500, nullable = false,name="title")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false,length = 30)
    private String author;
    @Column(name="modified_user",length = 30)
    private String modifiedUser;

    @Column(columnDefinition = "integer default 0")
    private Integer hit;

    private Boolean isActive;
    private Boolean isDelete;

    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    @Builder
    public Post(Long id,String title, String content, String author, String modifiedUser,Integer hit,Boolean isActive, Boolean isDelete
    ,LocalDateTime createDate, LocalDateTime modifiedDate){
        this.id = id;
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

//    @Builder
//    public Post(String title, String content, String author)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//
//    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    @OrderBy("id asc")
//    private List<Comment> comments = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
