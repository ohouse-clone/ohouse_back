package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.Post;
import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString

public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Boolean isActive;
    private String author;
    private LocalDateTime createDate;
    private Boolean isDelete;
    private Integer hit;
    private String modifiedUser;
    private LocalDateTime modifiedDate;

    public Post toEntity(){
        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .hit(hit)
                .isActive(isActive)
                .isDelete(isDelete)
                .modifiedDate(modifiedDate)
                .modifiedUser(modifiedUser)
                .build();
        return post;
    }

    @Builder
    public PostDto(Long id,String title, String content, String author, LocalDateTime createDate, Integer hit){
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.hit = hit;
    }

}
