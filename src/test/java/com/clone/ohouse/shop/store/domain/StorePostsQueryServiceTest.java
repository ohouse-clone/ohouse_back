package com.clone.ohouse.shop.store.domain;

import com.clone.ohouse.shop.product.domain.ItemService;
import com.clone.ohouse.shop.product.domain.access.CategoryRepository;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class StorePostsQueryServiceTest {
    @Autowired
    StorePostsService storePostsService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemService itemService;

    @BeforeEach
    void setup(){

    }
    @AfterEach
    void clean(){

    }

    @Test
    void getBundleViewV1(){

    }
    @Test
    void getBundleViewV2(){

    }
}