package com.clone.ohouse.shop.store.domain;

import com.clone.ohouse.shop.product.domain.ItemService;
import com.clone.ohouse.shop.product.domain.access.*;
import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.Category;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.Product;
import com.clone.ohouse.shop.store.domain.access.StorePostsRepository;
import com.clone.ohouse.shop.store.domain.access.StorePostsViewDto;
import com.clone.ohouse.shop.store.domain.dto.BundleVIewDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class StorePostsQueryServiceTest {

    @Autowired
    StorePostsQueryService storePostsQueryService;
    @Autowired
    StorePostsRepository storePostsRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemCategoryRepository itemCategoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setup() throws Exception{
        Category c1 = new Category("가구",20L);
        Category c2 = new Category("침대",22L);
        c2.addParent(c1);
        Category c3 = new Category("침대프레임",20L);
        c3.addParent(c2);
        Category c4 = new Category("일반침대",19L);
        c4.addParent(c3);
        Category c5 = new Category("수납침대",18L);
        c5.addParent(c3);
        Category c6 = new Category("저상형침대",17L);
        c6.addParent(c3);

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);
        categoryRepository.save(c4);
        categoryRepository.save(c5);
        categoryRepository.save(c6);

        CategorySearch condition1 = new CategorySearch(20L, 22L, 20L, 19L);
        Item item1 = new Bed("나무침대1", "MJH", "JHH", "BIG", "RED");
        Item item2 = new Bed("나무침대2", "MJH", "JHH", "BIG", "RED");
        Item item3 = new Bed("나무침대3", "MJH", "JHH", "BIG", "RED");
        Item item4 = new Bed("나무침대4", "MJH", "JHH", "BIG", "RED");

        CategorySearch condition2 = new CategorySearch(20L, 22L, 20L, 17L);
        Item item5 = new Bed("불편한침대5", "MJH", "JHH", "BIG", "RED");
        Item item6 = new Bed("불편한침대6", "MJH", "JHH", "BIG", "RED");
        Item item7 = new Bed("불편한침대7", "MJH", "JHH", "BIG", "RED");
        Item item8 = new Bed("불편한침대8", "MJH", "JHH", "BIG", "RED");

        itemService.save(item1, condition1);
        itemService.save(item2, condition1);
        itemService.save(item3, condition1);
        itemService.save(item4, condition1);
        itemService.save(item5, condition2);
        itemService.save(item6, condition2);
        itemService.save(item7, condition2);
        itemService.save(item8, condition2);

        Product product11 = new Product(item1, "제품11", 5001, 100, 0, 0L);
        Product product12 = new Product(item1, "제품12", 5002, 100, 0, 1L);
        Product product21 = new Product(item2, "제품21", 5003, 100, 0, 2L);
        Product product22 = new Product(item2, "제품22", 5004, 100, 0, 3L);
        Product product31 = new Product(item3, "제품31", 5005, 100, 0, 4L);
        Product product32 = new Product(item3, "제품32", 5006, 100, 0, 5L);
        Product product41 = new Product(item4, "제품41", 5007, 100, 0, 6L);
        Product product42 = new Product(item4, "제품42", 5008, 100, 0, 0L);
        Product product51 = new Product(item5, "제품51", 5009, 100, 0, 0L);
        Product product52 = new Product(item5, "제품52", 5010, 100, 0, 0L);
        Product product61 = new Product(item6, "제품61", 5011, 100, 0, 0L);
        Product product62 = new Product(item6, "제품62", 5012, 100, 0, 99L);
        Product product71 = new Product(item7, "제품71", 5013, 100, 0, 0L);
        Product product72 = new Product(item7, "제품72", 5014, 100, 0, 0L);
        Product product81 = new Product(item8, "제품81", 5015, 100, 0, 0L);
        Product product82 = new Product(item8, "제품82", 5016, 100, 0, 0L);
        Product product83 = new Product(item8, "제품83", 5017, 100, 0, 100L);
        Product product84 = new Product(item8, "제품84", 5018, 100, 0, 0L);

        StorePosts post1 = new StorePosts("제목1", null, "작가1",  null,null);
        StorePosts post2 = new StorePosts("제목2", null, "작가2",  null,null);
        StorePosts post3 = new StorePosts("제목3", null, "작가3",  null,null);
        StorePosts post4 = new StorePosts("제목4", null, "작가4",  null,null);
        StorePosts post5 = new StorePosts("제목5", null, "작가5", null,null);
        StorePosts post6 = new StorePosts("제목6", null, "작가6",  null,null);
        StorePosts post7 = new StorePosts("제목7", null, "작가7",  null,null);
        StorePosts post8 = new StorePosts("제목8", null, "작가8",  null,null);

        storePostsRepository.save(post1);
        storePostsRepository.save(post2);
        storePostsRepository.save(post3);
        storePostsRepository.save(post4);
        storePostsRepository.save(post5);
        storePostsRepository.save(post6);
        storePostsRepository.save(post7);
        storePostsRepository.save(post8);

        product11.registerStorePosts(post1);
        product12.registerStorePosts(post1);
        product21.registerStorePosts(post2);
        product22.registerStorePosts(post2);
        product31.registerStorePosts(post3);
        product32.registerStorePosts(post3);
        product41.registerStorePosts(post4);
        product42.registerStorePosts(post4);
        product51.registerStorePosts(post5);
        product52.registerStorePosts(post5);
        product61.registerStorePosts(post6);
        product62.registerStorePosts(post6);
        product71.registerStorePosts(post7);
        product72.registerStorePosts(post7);
        product81.registerStorePosts(post8);
        product82.registerStorePosts(post8);
        product83.registerStorePosts(post8);
        product84.registerStorePosts(post8);

        productRepository.save(product11);
        productRepository.save(product12);
        productRepository.save(product21);
        productRepository.save(product22);
        productRepository.save(product31);
        productRepository.save(product32);
        productRepository.save(product41);
        productRepository.save(product42);
        productRepository.save(product51);
        productRepository.save(product52);
        productRepository.save(product61);
        productRepository.save(product62);
        productRepository.save(product71);
        productRepository.save(product72);
        productRepository.save(product81);
        productRepository.save(product82);
        productRepository.save(product83);
        productRepository.save(product84);


    }
    @AfterEach
    void clean(){
        itemCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        productRepository.deleteAll();
        itemRepository.deleteAll();
        storePostsRepository.deleteAll();
    }

    @Test
    void getBundleViewV1(){
        CategorySearch condition = new CategorySearch(20L, 22L, 20L, 17L);
        Pageable pageable = PageRequest.of(0, 2);

        BundleVIewDto bundle = storePostsQueryService.getBundleViewV3(condition, pageable);

        System.out.println("-- result --");
        System.out.println("TotalNum : " + bundle.getTotalNum());
        System.out.println("postNum : " + bundle.getPostsNum());
        for (StorePostsViewDto viewDto : bundle.getPreviewPosts()) {
            System.out.println("viewDto = " + viewDto.getTitle() + ", price : " + viewDto.getPrice());
        }
    }

}