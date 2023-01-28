package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.ItemCategoryRepository;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.item.digital.RecommendNumber;
import com.clone.ohouse.store.domain.item.digital.Refrigerator;
import com.clone.ohouse.store.domain.item.digital.RefrigeratorCapacity;
import com.clone.ohouse.store.domain.item.digital.WashingMachine;
import com.clone.ohouse.store.domain.item.table.*;
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

    @Test
    void getBundleView3() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String category = "20_35_25_25";

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("category", category)
                .queryParam("deskcolor", "WHITE")
                .queryParam("page", "0")
                .queryParam("size", "4"));

        //then
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postsNum").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.previewPosts[0].title").value("제목10"));

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

        Category c7 = new Category("테이블", 35L);
        c7.addParent(c1);
        Category c8 = new Category("책상", 25L);
        c8.addParent(c7);
        Category c9 = new Category("일반책상", 25L);
        c9.addParent(c8);
        Category c10 = new Category("식탁", 26L);
        c10.addParent(c7);
        Category c11 = new Category("식탁", 26L);
        c11.addParent(c10);

        Category c12 = new Category("디지털", 30L);
        Category c13 = new Category("냉장고", 40L);
        c13.addParent(c12);
        Category c14 = new Category("일반냉장고", 30L);
        c14.addParent(c13);
        Category c15 = new Category("세탁기", 41L);
        c15.addParent(c12);
        Category c16 = new Category("일반세탁기", 31L);
        c16.addParent(c15);

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);
        categoryRepository.save(c4);
        categoryRepository.save(c5);
        categoryRepository.save(c6);
        categoryRepository.save(c7);
        categoryRepository.save(c8);
        categoryRepository.save(c9);
        categoryRepository.save(c10);
        categoryRepository.save(c11);
        categoryRepository.save(c12);
        categoryRepository.save(c13);
        categoryRepository.save(c14);
        categoryRepository.save(c15);
        categoryRepository.save(c16);

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

        CategorySearch condition3 = new CategorySearch(20L, 35L, 25L, 25L);
        Item item9 = new Desk("일반책상1", "DeskModel1", "BrandName1", DeskColor.BLACK, FrameMaterial.PLASTIC, UsageType.NORMAL);
        Item item10 = new Desk("일반책상2", "DeskModel2", "BrandName2", DeskColor.WHITE, FrameMaterial.WOOD, UsageType.STANDING);

        CategorySearch condition4 = new CategorySearch(20L, 35L, 26L, 26L);
        Item item11 = new DiningTable("식탁1", "식탁모델1", "dm1", DiningTableShape.CIRCLE, FrameMaterial.WOOD, NumberOfUsers.P2);
        Item item12 = new DiningTable("식탁2", "식탁모델2", "dm2", DiningTableShape.ELLIPSE, FrameMaterial.GLASS, NumberOfUsers.P3);

        CategorySearch condition5 = new CategorySearch(30L, 40L, 30L, null);
        Item item13 = new Refrigerator("냉장고1", "rm1", "bbn1", RefrigeratorCapacity.LESS_S50L);
        Item item14 = new Refrigerator("냉장고2", "rm2", "bbn2", RefrigeratorCapacity.S101L_MORE);

        CategorySearch condition6 = new CategorySearch(30L, 41L, 31L, null);
        Item item15 = new WashingMachine("일반세탁기1", "wsm1", "wsb1", RecommendNumber.P2);
        Item item16 = new WashingMachine("일반세탁기2", "wsm2", "wsb2", RecommendNumber.P1);

        itemService.save(item1, condition1);
        itemService.save(item2, condition1);
        itemService.save(item3, condition1);
        itemService.save(item4, condition1);
        itemService.save(item5, condition2);
        itemService.save(item6, condition2);
        itemService.save(item7, condition2);
        itemService.save(item8, condition2);

        itemService.save(item9, condition3);
        itemService.save(item10, condition3);
        itemService.save(item11, condition4);
        itemService.save(item12, condition4);
        itemService.save(item13, condition5);
        itemService.save(item14, condition5);
        itemService.save(item15, condition6);
        itemService.save(item16, condition6);

        Product product11 = new Product(item1, "제품11", 5001L, 100L, 0, 0L, null);
        Product product12 = new Product(item1, "제품12", 5002L, 100L, 0, 1L, null);
        Product product21 = new Product(item2, "제품21", 5003L, 100L, 0, 2L, null);
        Product product22 = new Product(item2, "제품22", 5004L, 100L, 0, 3L, null);
        Product product31 = new Product(item3, "제품31", 5005L, 100L, 0, 4L, null);
        Product product32 = new Product(item3, "제품32", 5006L, 100L, 0, 5L, null);
        Product product41 = new Product(item4, "제품41", 5007L, 100L, 0, 6L, null);
        Product product42 = new Product(item4, "제품42", 5008L, 100L, 0, 0L, null);
        Product product51 = new Product(item5, "제품51", 5009L, 100L, 0, 0L, null);
        Product product52 = new Product(item5, "제품52", 5010L, 100L, 0, 0L, null);
        Product product61 = new Product(item6, "제품61", 5011L, 100L, 0, 0L, null);
        Product product62 = new Product(item6, "제품62", 5012L, 100L, 0, 99L, null);
        Product product71 = new Product(item7, "제품71", 5013L, 100L, 0, 0L, null);
        Product product72 = new Product(item7, "제품72", 5014L, 100L, 0, 0L, null);
        Product product81 = new Product(item8, "제품81", 5015L, 100L, 0, 0L, null);
        Product product82 = new Product(item8, "제품82", 5016L, 100L, 0, 0L, null);
        Product product83 = new Product(item8, "제품83", 5017L, 100L, 0, 100L, null);
        Product product84 = new Product(item8, "제품84", 5018L, 100L, 0, 0L, null);

        Product product91 = new Product(item9, "제품91", 9001L, 90L, 0, 0L, null);
        Product product92 = new Product(item9, "제품92", 9002L, 91L, 0, 0L, null);
        Product product101 = new Product(item10, "제품101", 1000L, 100L, 0, 0L, null);
        Product product102 = new Product(item10, "제품102", 1000L, 100L, 0, 0L, null);
        Product product111 = new Product(item11, "제품111", 1000L, 100L, 0, 0L, null);
        Product product112 = new Product(item11, "제품112", 1000L, 100L, 0, 0L, null);
        Product product121 = new Product(item12, "제품121", 1000L, 100L, 0, 0L, null);
        Product product122 = new Product(item12, "제품122", 1000L, 100L, 0, 0L, null);
        Product product131 = new Product(item13, "제품131", 1000L, 100L, 0, 0L, null);
        Product product132 = new Product(item13, "제품132", 1000L, 100L, 0, 0L, null);
        Product product141 = new Product(item14, "제품141", 1000L, 100L, 0, 0L, null);
        Product product142 = new Product(item14, "제품142", 1000L, 100L, 0, 0L, null);
        Product product151 = new Product(item15, "제품151", 1000L, 100L, 0, 0L, null);
        Product product152 = new Product(item15, "제품152", 1000L, 100L, 0, 0L, null);
        Product product161 = new Product(item16, "제품161", 1000L, 100L, 0, 0L, null);
        Product product162 = new Product(item16, "제품162", 1000L, 100L, 0, 0L, null);



        StorePosts post1 = new StorePosts("제목1", null, "작가1",  null,null);
        StorePosts post2 = new StorePosts("제목2", null, "작가2",  null,null);
        StorePosts post3 = new StorePosts("제목3", null, "작가3",  null,null);
        StorePosts post4 = new StorePosts("제목4", null, "작가4",  null,null);
        StorePosts post5 = new StorePosts("제목5", null, "작가5", null,null);
        StorePosts post6 = new StorePosts("제목6", null, "작가6",  null,null);
        StorePosts post7 = new StorePosts("제목7", null, "작가7",  null,null);
        StorePosts post8 = new StorePosts("제목8", null, "작가8",  null,null);

        StorePosts post9 = new StorePosts("제목9", null, "작가9",  null,null);
        StorePosts post10 = new StorePosts("제목10", null, "작가10",  null,null);
        StorePosts post11 = new StorePosts("제목11", null, "작가11",  null,null);
        StorePosts post12 = new StorePosts("제목12", null, "작가12",  null,null);
        StorePosts post13 = new StorePosts("제목13", null, "작가13",  null,null);
        StorePosts post14 = new StorePosts("제목14", null, "작가14",  null,null);
        StorePosts post15 = new StorePosts("제목15", null, "작가15",  null,null);
        StorePosts post16 = new StorePosts("제목16", null, "작가16",  null,null);

        storePostsRepository.save(post1);
        storePostsRepository.save(post2);
        storePostsRepository.save(post3);
        storePostsRepository.save(post4);
        storePostsRepository.save(post5);
        storePostsRepository.save(post6);
        storePostsRepository.save(post7);
        storePostsRepository.save(post8);
        storePostsRepository.save(post9);
        storePostsRepository.save(post10);
        storePostsRepository.save(post11);
        storePostsRepository.save(post12);
        storePostsRepository.save(post13);
        storePostsRepository.save(post14);
        storePostsRepository.save(post15);
        storePostsRepository.save(post16);

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
        product91.registerStorePosts(post9);
        product92.registerStorePosts(post9);
        product101.registerStorePosts(post10);
        product102.registerStorePosts(post10);
        product111.registerStorePosts(post11);
        product112.registerStorePosts(post11);
        product121.registerStorePosts(post12);
        product122.registerStorePosts(post12);
        product131.registerStorePosts(post13);
        product132.registerStorePosts(post13);
        product141.registerStorePosts(post14);
        product142.registerStorePosts(post14);
        product151.registerStorePosts(post15);
        product152.registerStorePosts(post15);
        product161.registerStorePosts(post16);
        product162.registerStorePosts(post16);


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

        productRepository.save(product91);
        productRepository.save(product92);
        productRepository.save(product101);
        productRepository.save(product102);
        productRepository.save(product111);
        productRepository.save(product112);
        productRepository.save(product121);
        productRepository.save(product122);
        productRepository.save(product131);
        productRepository.save(product132);
        productRepository.save(product141);
        productRepository.save(product142);
        productRepository.save(product151);
        productRepository.save(product152);
        productRepository.save(product161);
        productRepository.save(product162);
    }


}