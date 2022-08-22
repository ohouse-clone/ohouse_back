package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.Comment;
import lombok.*;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString


public class CommentDto {
    private String commentTitle;
    private String commentContent;
    private String commentAuthor;

    public Comment toEntity(){
        Comment comment = Comment.builder()
                .commentTitle(commentTitle)
                .commentContent(commentContent)
                .commentAuthor(commentAuthor)
                .build();
        return comment;
    }

    @Builder
    public CommentDto(String commentTitle, String commentContent,String commentAuthor){
        this.commentTitle = commentTitle;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
    }
}
