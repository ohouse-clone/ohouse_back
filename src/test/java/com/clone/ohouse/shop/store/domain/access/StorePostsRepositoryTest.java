package com.clone.ohouse.shop.store.domain.access;

import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StorePostsRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorePostsRepository storePostsRepository;

    @BeforeEach
    public void previouslySetup(){
        productRepository.save(new Product());
    }

    @AfterEach
    public void clean(){
        storePostsRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void save(){
        //given
        String title = "제목";
        String content = "내용";
        String author = "나";

        StorePosts storePosts = new StorePosts(title, content.getBytes(StandardCharsets.UTF_8), author, null);

        //when
        storePostsRepository.save(storePosts);

        //then
        List<StorePosts> all = storePostsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(storePosts.getTitle());
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(storePosts.getAuthor());
    }


    @Test
    void delete(){
        //given
        String title = "제목";
        String content = "내용";
        String author = "나";

        StorePosts storePosts = new StorePosts(title, content.getBytes(StandardCharsets.UTF_8), author, null);
        StorePosts saved = storePostsRepository.save(storePosts);
        //when
        List<StorePosts> all = storePostsRepository.findAll();

        storePostsRepository.delete(all.get(0));

        Assertions.assertThat(storePostsRepository.count()).isEqualTo(0);
    }
}
