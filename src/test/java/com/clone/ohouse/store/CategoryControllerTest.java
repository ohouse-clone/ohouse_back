package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.dto.CategoryRequestDto;
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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @LocalServerPort
    private int port;
    private MockMvc mvc;
    @Autowired private WebApplicationContext context;
    private final String mappingUrl = "/store/api/v1/category";
    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void clean() {
        categoryRepository.deleteAll();
    }

    @Test
    void save() throws Exception{
        //given
        String url = "http://localhost:"+port+mappingUrl;
        String name = "가구";
        Long code = 20L;
        CategoryRequestDto requestDto1 = new CategoryRequestDto(name, code);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto1)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long saveId = new ObjectMapper().readValue(perform.andReturn().getResponse().getContentAsString(), Long.class);
        Category savedCategory = categoryRepository.findById(saveId).orElseThrow(() -> new IllegalArgumentException("Save Fail"));
        Assertions.assertThat(savedCategory.getName()).isEqualTo(name);
        Assertions.assertThat(savedCategory.getCode()).isEqualTo(code);
    }

    @Test
    void saveWithDozen() throws Exception{
        //given
        String url = "http://localhost:"+port+mappingUrl;
        String name = "가구";
        Long code = 20L;
        CategoryRequestDto requestDto1 = new CategoryRequestDto(name, code);
        Category saveCategory = categoryRepository.save(requestDto1.toEntity());

        CategoryRequestDto requestDto2 = new CategoryRequestDto("침대", 22L, saveCategory.getId());

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto2)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long saveId = new ObjectMapper().readValue(perform.andReturn().getResponse().getContentAsString(), Long.class);

        Category savedCategory = categoryRepository.findById(saveId).orElseThrow(() -> new IllegalArgumentException("Save Fail"));
        Assertions.assertThat(savedCategory.getName()).isEqualTo("침대");
        Assertions.assertThat(savedCategory.getCode()).isEqualTo(22L);

        Category parentCategory = categoryRepository.findById(savedCategory.getParent().getId()).orElseThrow(() -> new IllegalArgumentException("Save Fail"));
        Assertions.assertThat(parentCategory.getName()).isEqualTo("가구");
        Assertions.assertThat(parentCategory.getCode()).isEqualTo(20L);
    }

    @Transactional
    @Test
    void update() throws Exception{
        //given
        String url = "http://localhost:"+port+mappingUrl + "/";
        Category savedCategory1 = categoryRepository.save(new Category("가구", 20L));
        Category savedCategory1_1 = new Category("침대", 22L);
        savedCategory1_1.addParent(savedCategory1);

        Category savedCategory2 = categoryRepository.save(new Category("패브릭", 21L));
        categoryRepository.save(savedCategory1_1);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.put(url + savedCategory1_1.getId().toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(
                        new CategoryRequestDto("누누", 30L, savedCategory2.getId())
                )));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        Category category1_1 = categoryRepository.findById(savedCategory1_1.getId()).orElseThrow(() -> new RuntimeException("Fail to search category"));
        Assertions.assertThat(category1_1.getName()).isEqualTo("누누");
        Assertions.assertThat(category1_1.getCode()).isEqualTo(30L);
        Assertions.assertThat(category1_1.getParent()).isEqualTo(savedCategory2);
    }

    @Transactional
    @Test
    void findById() throws Exception{
        //given
        String url = "http://localhost:"+port+mappingUrl + "/";
        Category savedCategory1 = categoryRepository.save(new Category("가구", 20L));
        Category savedCategory1_1 = new Category("침대", 22L);
        savedCategory1_1.addParent(savedCategory1);
        Category save = categoryRepository.save(savedCategory1_1);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url + save.getId().toString()));


        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("침대"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(22L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").value(savedCategory1.getId()));
    }

    @Test
    void delete() throws Exception {
    //given
        String url = "http://localhost:"+port+mappingUrl + "/";
        Category savedCategory1 = categoryRepository.save(new Category("가구", 20L));
        Category savedCategory1_1 = new Category("침대", 22L);
        savedCategory1_1.addParent(savedCategory1);
        Category save = categoryRepository.save(savedCategory1_1);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete(url + save.getId().toString()));


        //then
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> categoryRepository.findById(save.getId()).orElseThrow(()->new NoSuchElementException("찾으려는 id가 잘못된 id입니다.")));
    }

}