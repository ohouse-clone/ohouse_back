package com.clone.ohouse.community.controller;

import com.clone.ohouse.community.entity.Comment;
import com.clone.ohouse.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller

public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/create_comment")
    public Comment commentCreate(@RequestBody Comment requestbody){
        String commentTitle = requestbody.getCommentTitle();
        String commentContent = requestbody.getCommentContent();
        Comment comment = Comment.builder().build();
        return comment;
    }

    @PostMapping("/delete_comment")
    public void deleteComment(@RequestBody Comment requestbody){
        Long commentId = requestbody.getId();
        commentService.deleteById(commentId);
    }

    @PostMapping("/update_comment")
    public void updateComment(@RequestBody Comment requestbody){
        Long newId = requestbody.getId();
        //commentService.updateComment(newId);
    }

    @PostMapping("/read_comment")
    public void readComment(@RequestBody Comment requestbody){
        commentService.findAll();
    }
}

