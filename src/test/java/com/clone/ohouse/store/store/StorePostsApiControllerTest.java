package com.clone.ohouse.store.store;

import com.clone.ohouse.store.domain.StorePostsService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StorePostsApiControllerTest {

    @Autowired
    private StorePostsRepository storePostsRepository;
    @Autowired
    private StorePostsService boardService;

    @LocalServerPort
    private int port;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    private final String mappingUrl = "/store/productions";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void clean() {
        storePostsRepository.deleteAll();
    }

    @Test
    void save() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
        String title = "상품제목";
        String content = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;
        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, content, null, author);

        //when
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(saveRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //then
        List<StorePosts> all = storePostsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContentUrl()).isEqualTo(content);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
        System.out.println("saved Time : " + all.get(0).getCreatedDate());

    }

    @Test
    void update() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String contentUrl = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;

        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, contentUrl, null, author);
        Long savedId = boardService.save(saveRequestDto);

        String title2 = "제목없음";
        String contentUrl2 = "url";
        String modifiedUser2 = "WWA";
        StorePostsUpdateRequestDto updateRequestDto = StorePostsUpdateRequestDto.builder()
                .title(title2)
                .contentUrl(contentUrl2)
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
        Assertions.assertThat(all.get(0).getContentUrl()).isEqualTo(contentUrl2);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
        Assertions.assertThat(all.get(0).getModifiedUser()).isEqualTo(modifiedUser2);
    }

    @Test
    void findById() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String content = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;

        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, content, null, author);
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
        String contentUrl = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;

        StorePostsSaveRequestDto saveRequestDto = new StorePostsSaveRequestDto(title, contentUrl, null, author);
        Long savedId = boardService.save(saveRequestDto);

        //when
        mvc.perform(MockMvcRequestBuilders.delete(url + String.valueOf(savedId)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertThat(storePostsRepository.count()).isEqualTo(0);
    }
}