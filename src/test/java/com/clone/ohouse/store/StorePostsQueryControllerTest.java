package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.ItemCategoryRepository;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StorePostsQueryControllerTest {

    @LocalServerPort private int port;
    @Autowired private WebApplicationContext context;
    private MockMvc mvc;
    private final String mappingUrl = "/store/category";

    @Autowired StorePostsRepository storePostsRepository;
    @Autowired ProductRepository productRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ItemService itemService;
    @Autowired ItemCategoryRepository itemCategoryRepository;
    @Autowired ItemRepository itemRepository;

    @BeforeEach
    public void setup() throws Exception{
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        saveData();
    }

    @AfterEach
    public void clean(){
        itemCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        productRepository.deleteAll();
        itemRepository.deleteAll();
        storePostsRepository.deleteAll();
    }

    @Test
    void getBundleView1() throws Exception{
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String key = "bedcolor";
        String category = "20_22_20_20";
        String color1 = "BLUE";
        String color2 = "WHITE";

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("category",category)
                .queryParam(key, color1, color2)
                .queryParam("page", "1")
                .queryParam("size", "1"));

        //then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postsNum").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[0].title").value("제목4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[0].popular").value(6));
    }

    @Test
    void getBundleView2() throws Exception{
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String category = "20_22_20";


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("category",category)
                .queryParam("page", "0")
                .queryParam("size", "3"));

        //then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postsNum").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[0].title").value("제목8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[1].title").value("제목6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[0].popular").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[1].popular").value(99));
    }


    private void saveData() throws Exception{
        Category c1 = new Category("가구",20L);
        Category c2 = new Category("침대",22L);
        c2.addParent(c1);
        Category c3 = new Category("침대프레임",20L);
        c3.addParent(c2);
        Category c4 = new Category("일반침대",20L);
        c4.addParent(c3);
        Category c5 = new Category("수납침대",21L);
        c5.addParent(c3);
        Category c6 = new Category("저상형침대",22L);
        c6.addParent(c3);

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);
        categoryRepository.save(c4);
        categoryRepository.save(c5);
        categoryRepository.save(c6);

        CategorySearch condition1 = new CategorySearch(20L, 22L, 20L, 20L);
        Item item1 = new Bed("나무침대1", "MJH", "JHH", BedSize.K, BedColor.WHITE);
        Item item2 = new Bed("나무침대2", "MJH", "JHH", BedSize.K, BedColor.RED);
        Item item3 = new Bed("나무침대3", "MJH", "JHH", BedSize.K, BedColor.BLUE);
        Item item4 = new Bed("나무침대4", "MJH", "JHH", BedSize.K, BedColor.WHITE);

        CategorySearch condition2 = new CategorySearch(20L, 22L, 20L, 21L);
        Item item5 = new StorageBed("불편한침대5", "MJH", "JHH", Material.FAKE_LEATHER);
        Item item6 = new StorageBed("불편한침대6", "MJH", "JHH", Material.FAKE_LEATHER);
        Item item7 = new StorageBed("불편한침대7", "MJH", "JHH", Material.FAKE_LEATHER);
        Item item8 = new StorageBed("불편한침대8", "MJH", "JHH", Material.STEEL);

        itemService.save(item1, condition1);
        itemService.save(item2, condition1);
        itemService.save(item3, condition1);
        itemService.save(item4, condition1);
        itemService.save(item5, condition2);
        itemService.save(item6, condition2);
        itemService.save(item7, condition2);
        itemService.save(item8, condition2);

        Product product11 = new Product(item1, "제품11", 5001, 100, 0, 0L, null);
        Product product12 = new Product(item1, "제품12", 5002, 100, 0, 1L, null);
        Product product21 = new Product(item2, "제품21", 5003, 100, 0, 2L, null);
        Product product22 = new Product(item2, "제품22", 5004, 100, 0, 3L, null);
        Product product31 = new Product(item3, "제품31", 5005, 100, 0, 4L, null);
        Product product32 = new Product(item3, "제품32", 5006, 100, 0, 5L, null);
        Product product41 = new Product(item4, "제품41", 5007, 100, 0, 6L, null);
        Product product42 = new Product(item4, "제품42", 5008, 100, 0, 0L, null);
        Product product51 = new Product(item5, "제품51", 5009, 100, 0, 0L, null);
        Product product52 = new Product(item5, "제품52", 5010, 100, 0, 0L, null);
        Product product61 = new Product(item6, "제품61", 5011, 100, 0, 0L, null);
        Product product62 = new Product(item6, "제품62", 5012, 100, 0, 99L, null);
        Product product71 = new Product(item7, "제품71", 5013, 100, 0, 0L, null);
        Product product72 = new Product(item7, "제품72", 5014, 100, 0, 0L, null);
        Product product81 = new Product(item8, "제품81", 5015, 100, 0, 0L, null);
        Product product82 = new Product(item8, "제품82", 5016, 100, 0, 0L, null);
        Product product83 = new Product(item8, "제품83", 5017, 100, 0, 100L, null);
        Product product84 = new Product(item8, "제품84", 5018, 100, 0, 0L, null);

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


}