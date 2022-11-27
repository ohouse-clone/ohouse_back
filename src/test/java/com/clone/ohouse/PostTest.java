package com.clone.ohouse;

import com.clone.ohouse.community.entity.Post;
import com.clone.ohouse.community.repository.PostRepository;
import com.clone.ohouse.community.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional

public class PostTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void createPost() throws Exception{
        Post post = Post.builder()
                .author("user1")
                .title("newpost")
                .content("thisisnew")
                .build();
        Post post2 = Post.builder()
                .author("uwer2")
                .title("post2")
                .content("newone")
                .build();

        Post new2 = postRepository.save(post2);
        //저장
        Post newPost = postRepository.save(post);
        //조회
        Post result = postRepository.findById(post.getId()).get();
        Optional<Post> findByIdPost  = postRepository.findById(post.getId());
        Optional<Post> findByTitlePost = postRepository.findByTitle(post.getTitle());

        assertThat(findByIdPost.get().getId()).isEqualTo(post.getId());
        assertThat(findByIdPost.get()).isEqualTo(post);
        assertThat(findByTitlePost.get().getId()).isEqualTo(post.getId());
        assertThat(findByTitlePost.get()).isEqualTo(post);
    }

    @Test
    public void CRUD() throws Exception{
        Post post1 = Post.builder()
                .author("author1")
                .title("post1")
                .content("newpost1")
                .build();

        Post post2 = Post.builder()
                .author("author2")
                .title("post2")
                .content("newpost2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        Post findPost1 = postRepository.findById(post1.getId()).get();
        Post findPost2 = postRepository.findById(post2.getId()).get();
        assertThat(findPost1).isEqualTo(post1);
        assertThat(findPost2).isEqualTo(post2);

        List<Post> posts = postRepository.findAll();
        long count = postRepository.count();

        assertThat(posts.size()).isEqualTo(count);
        assertThat(posts.size()).isEqualTo(2);

        postRepository.delete(post1);
        postRepository.delete(post2);
        long deletedCount = postRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }
}
