package com.clone.ohouse.community.domain.comment.dto;

import com.clone.ohouse.community.domain.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString


public class CommentDto {
    private String commentTitle;
    private String commentContent;
    private String commentAuthor;
    private LocalDateTime createdDate;

    public Comment toEntity(){
        Comment comment = Comment.builder()
                .commentTitle(commentTitle)
                .commentContent(commentContent)
                .commentAuthor(commentAuthor)
                .createdDate(createdDate)
                .build();
        return comment;
    }

    @Builder
    public CommentDto(String commentTitle, String commentContent,String commentAuthor,LocalDateTime createdDate){
        this.commentTitle = commentTitle;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.createdDate = createdDate;
    }
}
