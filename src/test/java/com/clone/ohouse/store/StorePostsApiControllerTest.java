package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ProductService;
import com.clone.ohouse.store.domain.StorePostsService;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.dto.ProductSaveRequestDto;
import com.clone.ohouse.store.domain.product.dto.ProductStorePostIdUpdateRequestDto;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsUpdateRequestDto;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StorePostsApiControllerTest {

    @Autowired
    private StorePostsRepository storePostsRepository;
    @Autowired
    private StorePostsService boardService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @LocalServerPort
    private int port;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    private final String mappingUrl = "/store/api/v1/post";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void clean() {
        productRepository.deleteAll();
        storePostsRepository.deleteAll();
    }

    @Test
    void save() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String author = "JJH";
        String modifiedUser = null;
        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, null, null, author);

        //when
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(saveRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //then
        List<StorePosts> all = storePostsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
        System.out.println("saved Time : " + all.get(0).getCreatedDate());

    }

    @Test
    void update() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String author = "JJH";
        String modifiedUser = null;

        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, null, null, author);
        Long savedId = boardService.save(saveRequestDto);

        String title2 = "제목없음";
        String modifiedUser2 = "WWA";
        StorePostsUpdateRequestDto updateRequestDto = StorePostsUpdateRequestDto.builder()
                .title(title2)
                .contentImageId(null)
                .modifiedUser(modifiedUser2)
                .build();

        //when
        mvc.perform(MockMvcRequestBuilders.put(url + savedId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        //then
        List<StorePosts> all = storePostsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title2);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
        Assertions.assertThat(all.get(0).getModifiedUser()).isEqualTo(modifiedUser2);
    }

    @Test
    void findById() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String author = "JJH";
        String modifiedUser = null;

        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, null, null, author);
        Long savedId = boardService.save(saveRequestDto);

        //when && then
        mvc.perform(MockMvcRequestBuilders.get(url + String.valueOf(savedId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title));


    }

    @Test
    void delete() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String author = "JJH";
        String modifiedUser = null;

        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, null, null, author);
        Long savedId = boardService.save(saveRequestDto);

        //when
        mvc.perform(MockMvcRequestBuilders.delete(url + String.valueOf(savedId)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertThat(storePostsRepository.count()).isEqualTo(0);
    }

    @Transactional
    @Test
    void findByIdWithProduct() throws Exception{
        //given
        String url = "http://localhost:" + port + mappingUrl + "/productswith/";
        Long saveProductId1 = productService.save(new ProductSaveRequestDto(null, "제품1", 1000, 100, 10, null));
        Long saveProductId2 = productService.save(new ProductSaveRequestDto(null, "제품2", 2000, 200, 20, null));
        Long saveProductId3 = productService.save(new ProductSaveRequestDto(null, "제품3", 3000, 300, 30, null));
        Long savePostId1 = boardService.save(new StorePostsSaveRequestDto("제목1", null, null, "jh1"));
        productService.updateWithStorePostId(new ProductStorePostIdUpdateRequestDto(savePostId1, new ArrayList<>(
                List.of(saveProductId1, saveProductId2, saveProductId3)
        )));

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url + String.valueOf(savePostId1)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("제목1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.author").value("jh1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.productNum").value(3));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].productName").value("제품1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].productName").value("제품2"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[2].productName").value("제품3"));

    }
}