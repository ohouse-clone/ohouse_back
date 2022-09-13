package com.clone.ohouse.community.service;

import com.clone.ohouse.community.entity.Post;
import com.clone.ohouse.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        postRepository.findAll().forEach(s -> posts.add(s));
        return posts;
    }

    public Optional<Post> findByTitle(String title) {
        Optional<Post> post = postRepository.findByTitle(title);
        return post;
    }

    public void deleteByTitle(String title) {
        Optional<Post> deletePost = postRepository.findByTitle(title);
        postRepository.delete(deletePost.orElseThrow(() ->
                new NoSuchElementException("POST NOT FOUND")));
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public Post save(Post post) {
        postRepository.save(post);
        return post;
    }

    public void updatePost(String title) {
        Optional<Post> post = postRepository.findByTitle(title);
        post.ifPresent(newPost -> {
            post.get().setTitle(newPost.getTitle());
            post.get().setContent(newPost.getContent());
            post.get().setAuthor(newPost.getAuthor());
            post.get().setId(newPost.getId());
        });
    }


}
