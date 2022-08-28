package com.clone.ohouse.shop.board;

import com.clone.ohouse.shop.board.domain.ProductBoardService;
import com.clone.ohouse.shop.board.domain.access.ProductBoardRepository;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardSaveRequestDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardUpdateRequestDto;
import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcExtensionsKt;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductBoardApiControllerTest {

    @Autowired
    private ProductBoardRepository productBoardRepository;
    @Autowired
    private ProductBoardService boardService;

    @LocalServerPort
    private int port;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    private final String mappingUrl = "/store/productions";

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void clean() {
        productBoardRepository.deleteAll();
    }

    @Test
    void save() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
        String title = "상품제목";
        String content = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;
        ProductBoardSaveRequestDto saveRequestDto = new ProductBoardSaveRequestDto(title, content, author, modifiedUser);

        //when
        mvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(saveRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        List<ProductBoard> all = productBoardRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(new String(all.get(0).getContent(), StandardCharsets.UTF_8)).isEqualTo(content);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
        System.out.println("saved Time : " + all.get(0).getCreatedDate());

    }

    @Test
    void update() throws Exception{
        //given
        String url = "http://localhost:" + port + mappingUrl + "/";
        String title = "상품제목";
        String content = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;

        ProductBoardSaveRequestDto saveRequestDto = new ProductBoardSaveRequestDto(title, content, author, modifiedUser);
        Long savedId = boardService.save(saveRequestDto);

        String title2 = "제목없음";
        String content2 = "정갈";
        String modifiedUser2 = "WWA";
        ProductBoardUpdateRequestDto updateRequestDto = ProductBoardUpdateRequestDto.builder()
                .title(title2)
                .content(content2)
                .modifiedUser(modifiedUser2)
                .build();

        //when
        mvc.perform(MockMvcRequestBuilders.put(url + savedId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        //then
        List<ProductBoard> all = productBoardRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title2);
        Assertions.assertThat(new String(all.get(0).getContent(), StandardCharsets.UTF_8)).isEqualTo(content2);
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

        ProductBoardSaveRequestDto saveRequestDto = new ProductBoardSaveRequestDto(title, content, author, modifiedUser);
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
        String content = "klasdfjlkj34t42363gjerwovm";
        String author = "JJH";
        String modifiedUser = null;

        ProductBoardSaveRequestDto saveRequestDto = new ProductBoardSaveRequestDto(title, content, author, modifiedUser);
        Long savedId = boardService.save(saveRequestDto);

        //when
        mvc.perform(MockMvcRequestBuilders.delete(url + String.valueOf(savedId)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertThat(productBoardRepository.count()).isEqualTo(0);
    }
}