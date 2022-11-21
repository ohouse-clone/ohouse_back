package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.*;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.item.bed.dto.BedRequestDto;
import com.clone.ohouse.store.domain.item.bed.dto.StorageBedRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.util.NoSuchElementException;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemApiControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemCategoryRepository itemCategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;


    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        setupCategory();
    }
    @AfterEach
    public void clean(){
        itemCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void saveWithBed() throws Exception {
        //given
        String url = "http://localhost:" + port + "/store/item/bed";
        String category = "20_22_20_20";
        BedRequestDto requestDto = new BedRequestDto("testbed", "model", "emart", BedColor.BLUE, BedSize.K);
        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(url)
                .queryParam("category", category)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto)));


        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long savedId = new ObjectMapper().readValue(perform.andReturn().getResponse().getContentAsString(), Long.class);
        Item savedItem = itemRepository.findById(savedId).orElseThrow(() -> new RuntimeException("잘못된 Item id값"));
        Assertions.assertThat(((Bed)savedItem).getColor()).isEqualTo(BedColor.BLUE);
        Assertions.assertThat(((Bed)savedItem).getName()).isEqualTo("testbed");
        Assertions.assertThat(savedItem instanceof Bed).isTrue();
    }

    @Test
    void saveFailWithBedForTypeMiss() throws Exception{
        //given
        String url = "http://localhost:" + port + "/store/item/bed";
        String category = "20_22_20_21";
        BedRequestDto requestDto = new BedRequestDto("testbed", "model", "emart", BedColor.BLUE, BedSize.K);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(url)
                .queryParam("category", category)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveWithStorageBed() throws Exception {
        //given
        String url = "http://localhost:" + port + "/store/item/storagebed";
        String category = "20_22_20_21";
        //BedRequestDto requestDto = new BedRequestDto("testbed", "model", "emart", BedColor.BLUE, BedSize.K);
        StorageBedRequestDto requestDto = new StorageBedRequestDto("testbed2", "model2", "emart2", Material.FAKE_WOOD);
        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(url)
                .queryParam("category", category)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto)));


        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long savedId = new ObjectMapper().readValue(perform.andReturn().getResponse().getContentAsString(), Long.class);
        Item savedItem = itemRepository.findById(savedId).orElseThrow(() -> new RuntimeException("잘못된 Item id값"));
        Assertions.assertThat(((StorageBed)savedItem).getMaterial()).isEqualTo(Material.FAKE_WOOD);
        Assertions.assertThat(((StorageBed)savedItem).getName()).isEqualTo("testbed2");
        Assertions.assertThat(savedItem instanceof StorageBed).isTrue();
    }

    @Transactional
    @Test
    void findByCategory() throws Exception {
        //given
        String url = "http://localhost:" + port + "/store/items";
        String category1 = "20_22_20_20";
        String category2 = "20_22_20_21";
        CategorySearch condition1 = CategoryParser.parseCategoryString(category1);
        CategorySearch condition2 = CategoryParser.parseCategoryString(category2);
        itemService.save(new Bed("침대1","모델1","브랜드1",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대2","모델2","브랜드2",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대3","모델3","브랜드3",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대4","모델4","브랜드4",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new StorageBed("수납1", "수모1", "수모브1", Material.FAKE_LEATHER), condition2);
        itemService.save(new StorageBed("수납2", "수모2", "수모브2", Material.FAKE_LEATHER), condition2);
        itemService.save(new StorageBed("수납3", "수모3", "수모브3", Material.FAKE_LEATHER), condition2);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("category", category1)
                .queryParam("page", "0")
                .queryParam("size", "3"));


        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(4L));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.itemNum").value(3L));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name").value("침대1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name").value("침대2"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name").value("침대3"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].size").value("K"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[1].color").value("BLUE"));
    }

    @Transactional
    @Test
    void findByCategoryWith2Condition() throws Exception {
        //given
        String url = "http://localhost:" + port + "/store/items";
        String category1 = "20_22_20_20";
        String category2 = "20_22_20_21";
        CategorySearch condition1 = CategoryParser.parseCategoryString(category1);
        CategorySearch condition2 = CategoryParser.parseCategoryString(category2);
        itemService.save(new Bed("침대1","모델1","브랜드1",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대2","모델2","브랜드2",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대3","모델3","브랜드3",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대4","모델4","브랜드4",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new StorageBed("수납1", "수모1", "수모브1", Material.FAKE_LEATHER), condition2);
        itemService.save(new StorageBed("수납2", "수모2", "수모브2", Material.FAKE_LEATHER), condition2);
        itemService.save(new StorageBed("수납3", "수모3", "수모브3", Material.FAKE_LEATHER), condition2);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("category", category1)
                .queryParam("page", "0")
                .queryParam("size", "3")
                .queryParam("itemName", "침대3")
                .queryParam("itemBrandName", "모델2"));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    @Transactional
    @Test
    void findByCategoryWith1Condition() throws Exception {
        //given
        String url = "http://localhost:" + port + "/store/items";
        String category1 = "20_22_20_20";
        String category2 = "20_22_20_21";
        CategorySearch condition1 = CategoryParser.parseCategoryString(category1);
        CategorySearch condition2 = CategoryParser.parseCategoryString(category2);
        itemService.save(new Bed("침대1","모델1","브랜드1",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대2","모델2","브랜드2",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대3","모델3-1","브랜드3",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대3","모델3-2","브랜드3",BedSize.CK, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대3","모델3-3","브랜드3",BedSize.LK, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대4","모델4","브랜드4",BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new StorageBed("수납1", "수모1", "수모브1", Material.FAKE_LEATHER), condition2);
        itemService.save(new StorageBed("수납2", "수모2", "수모브2", Material.FAKE_LEATHER), condition2);
        itemService.save(new StorageBed("수납3", "수모3", "수모브3", Material.FAKE_LEATHER), condition2);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("category", category1)
                .queryParam("page", "0")
                .queryParam("size", "3")
                .queryParam("itemName", "침대3"));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(3L));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.itemNum").value(3L));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name").value("침대3"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name").value("침대3"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name").value("침대3"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].size").value("K"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.items[2].size").value("LK"));
    }

    @Test
    void delete() throws Exception {
        //given
        String url = "http://localhost:" + port + "/store/item/";
        String category = "20_22_20_21";
        StorageBedRequestDto requestDto = new StorageBedRequestDto("testbed2", "model2", "emart2", Material.FAKE_WOOD);
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        Long savedId = itemService.save(requestDto.toEntity(), categorySearch);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete(url + savedId.toString()));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> itemRepository.findById(savedId).orElseThrow(() -> new NoSuchElementException("id의 item이 없습니다.")));
    }

    @Transactional
    private void setupCategory(){
        //setup category
        Category savedCategory1 = categoryRepository.save(new Category("가구", 20L));
        Category savedCategory1_1 = new Category("침대", 22L);
        Category savedCategory1_1_1 = new Category("침대프레임", 20L);
        Category savedCategory1_1_2 = new Category("침대+매트릭스", 21L);
        Category savedCategory1_1_3 = new Category("침대부속가구", 22L);
        Category savedCategory1_1_1_1 = new Category("일반침대", 20L);
        Category savedCategory1_1_1_2 = new Category("수납침대", 21L);

        savedCategory1_1.addParent(savedCategory1);
        savedCategory1_1_1.addParent(savedCategory1_1);
        savedCategory1_1_2.addParent(savedCategory1_1);
        savedCategory1_1_3.addParent(savedCategory1_1);
        savedCategory1_1_1_1.addParent(savedCategory1_1_1);
        savedCategory1_1_1_2.addParent(savedCategory1_1_1);
        categoryRepository.save(savedCategory1_1);
        categoryRepository.save(savedCategory1_1_1);
        categoryRepository.save(savedCategory1_1_2);
        categoryRepository.save(savedCategory1_1_3);
        categoryRepository.save(savedCategory1_1_1_1);
        categoryRepository.save(savedCategory1_1_1_2);
    }



}