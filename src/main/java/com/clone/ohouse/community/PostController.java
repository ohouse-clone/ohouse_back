package com.clone.ohouse.community;

import com.clone.ohouse.community.domain.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import com.clone.ohouse.community.domain.cardcollections.Post;
import org.springframework.web.bind.annotation.RequestBody;

@Controller

public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create_post")
    public Post createPost(@RequestBody Post requestbody) {
        String title = requestbody.getTitle();
        String content = requestbody.getContent();
        String author = requestbody.getAuthor();
        Post post = Post.builder().build();
        return post;
    }

    @PostMapping("/delete_post")
    public void deletePost(@RequestBody Post requestbody) {
        Long deletePost = requestbody.getId();
        postService.deleteById(deletePost);

    }

    @PostMapping("/update_post")
    public void updatePost(@RequestBody Post requestbody) {
        postService.updatePost(requestbody.getTitle());
    }

    @PostMapping("/read_post")
    public void getAllPost(@RequestBody Post requestbody) {
        postService.findAll();
    }
}
