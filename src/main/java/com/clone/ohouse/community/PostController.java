package com.clone.ohouse.community;

import com.clone.ohouse.community.domain.PostService;
import com.clone.ohouse.community.domain.cardcollections.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller

public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create_post")
    public Card createPost(@RequestBody Card requestbody) {
        String title = requestbody.getTitle();
        String content = requestbody.getContent();
        String author = requestbody.getAuthor();
        Card card = Card.builder().build();
        return card;
    }

    @PostMapping("/delete_post")
    public void deletePost(@RequestBody Card requestbody) {
        Long deletePost = requestbody.getId();
        postService.deleteById(deletePost);

    }

    @PostMapping("/update_post")
    public void updatePost(@RequestBody Card requestbody) {
        postService.updatePost(requestbody.getTitle());
    }

    @PostMapping("/read_post")
    public void getAllPost(@RequestBody Card requestbody) {
        postService.findAll();
    }
}
