package com.clone.ohouse.shop.store;

import com.clone.ohouse.shop.product.domain.access.BedRepository;
import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.product.domain.entity.Product;
import com.clone.ohouse.shop.store.domain.access.StorePostsRepository;
import com.clone.ohouse.shop.store.domain.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreCategoryApiControllerTest {
    private MockMvc mvc;
    private int port;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ItemCategoryCodeRepository itemCategoryCodeRepository;
    @Autowired
    private StorePostsRepository storePostsRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();


        ItemCategoryCode code = itemCategoryCodeRepository.findByCategory1AndCategory2AndCategory3AndCategory4(0, 22, 20, 20);
        StorePosts post1 = storePostsRepository.save(new StorePostsSaveRequestDto("제목1", null,null, "JJH").toEntity());
        StorePosts post2 = storePostsRepository.save(new StorePostsSaveRequestDto("제목2", null,null, "AAA").toEntity());
        StorePosts post3 = storePostsRepository.save(new StorePostsSaveRequestDto("제목3", null,null, "BBB").toEntity());
        StorePosts post4 = storePostsRepository.save(new StorePostsSaveRequestDto("제목4", null,null, "CCC").toEntity());

        Bed bed1 = bedRepository.save(Bed.builder().categoryCode(code).name("침대1").size("크기1").build());
        Bed bed2 = bedRepository.save(Bed.builder().categoryCode(code).name("침대2").size("크기2").build());
        Bed bed3 = bedRepository.save(Bed.builder().categoryCode(code).name("침대3").size("크기3").build());
        Bed bed4 = bedRepository.save(Bed.builder().categoryCode(code).name("침대4").size("크기4").build());

        Product product1 = Product.builder().productName("제품1").item(bed1).price(1000).build();
        Product product2 = Product.builder().productName("제품2").item(bed2).price(2000).build();
        Product product3 = Product.builder().productName("제품3").item(bed3).price(3000).build();
        Product product4 = Product.builder().productName("제품4").item(bed4).price(4000).build();

        product1.registerStorePosts(post1);
        product2.registerStorePosts(post2);
        product3.registerStorePosts(post3);
        product4.registerStorePosts(post4);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
    }

    @AfterEach
    void clean() {
        productRepository.deleteAll();
        storePostsRepository.deleteAll();
        bedRepository.deleteAll();
    }


    @Test
    public void getCategoryView() throws Exception {
        //given
        String mappingUrl = "/store/category";
        String url = "http://localhost:" + port + mappingUrl;
        //String query = "?category=0_22_20_20&order=popular&page=0&per=24";
        PageRequest request = PageRequest.of(0, 4);
        String category = "0_22_20_20";
        String order = "popular";
        String page = "0";
        String per = "24";
        Long size = storePostsRepository.count();
        String expectByTitle = "$..bundle[?(@.title == '%s')]";
        String expectByPrice = "$..bundle[?(@.price == '%s')]";

        //when && then
        mvc.perform(MockMvcRequestBuilders.get(url)
                        .param("category", category)
                        .param("order", order)
                        .param("page", page)
                        .param("per", per))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bundleSize").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath(expectByTitle, "제목1").exists())
                .andExpect(MockMvcResultMatchers.jsonPath(expectByPrice, "1000").exists());
    }
}