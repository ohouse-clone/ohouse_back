package com.clone.ohouse.community.entity;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name="comment")
    public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="comment_title",nullable = false, length = 40)
        private String commentTitle;
        @Column(name="comment_content",nullable = false)
        private String commentContent;
        @Column(name="comment_author",nullable = false,length = 30)
        private String commentAuthor;


        @Builder
    public Comment(String commentTitle, String commentContent,String commentAuthor){
            this.commentTitle = commentTitle;
            this.commentAuthor = commentAuthor;
            this.commentContent = commentContent;
        }

}
