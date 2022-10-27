package com.clone.ohouse.store.store.domain.access;

import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        String contentUrl = "url";
        String author = "나";

        StorePosts storePosts = new StorePosts(title, contentUrl, author, null, null);

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
        String contentUrl = "url";
        String author = "나";

        StorePosts storePosts = new StorePosts(title, contentUrl, author, null, null);
        StorePosts saved = storePostsRepository.save(storePosts);
        //when
        List<StorePosts> all = storePostsRepository.findAll();

        storePostsRepository.delete(all.get(0));

        Assertions.assertThat(storePostsRepository.count()).isEqualTo(0);
    }
}
