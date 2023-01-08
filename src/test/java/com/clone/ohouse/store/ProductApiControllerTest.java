package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.ProductService;
import com.clone.ohouse.store.domain.category.*;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSize;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.dto.ProductSaveRequestDto;
import com.clone.ohouse.store.domain.product.dto.ProductUpdateRequestDto;
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
class ProductApiControllerTest {
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
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;


    String mappingUrl = "/store/api/v1/product/";
    String baseUrl = "http://localhost:" + port + mappingUrl;
    Long saveItemId1 = null;
    Long saveItemId2 = null;
    Long saveItemId3 = null;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        setupCategory();

        //item setup
        CategorySearch categorySearch = CategoryParser.parseCategoryString("20_22_20_20");
        saveItemId1 = itemService.save(new Bed("침대1","모델1","브1", BedSize.CK, BedColor.RED), categorySearch);
        saveItemId2 = itemService.save(new Bed("침대2","모델2","브2", BedSize.CK, BedColor.RED), categorySearch);
        saveItemId3 = itemService.save(new Bed("침대3","모델3","브3", BedSize.CK, BedColor.RED), categorySearch);
    }
    @AfterEach
    public void clean(){
        itemCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        productRepository.deleteAll();
        itemRepository.deleteAll();
    }


    @Test
    public void save() throws Exception{
        //given
        String url = baseUrl + "/";
        ProductSaveRequestDto requestDto = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1")
                .price(1000L)
                .stock(100L)
                .rateDiscount(10)
                .build();

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long result = new ObjectMapper().readValue(perform.andReturn().getResponse().getContentAsString(), Long.class);
        Product savedProduct = productRepository.findById(result).orElseThrow(() -> new RuntimeException("잘못된 저장된 product id"));
        Assertions.assertThat(result).isEqualTo(savedProduct.getId());
        Assertions.assertThat(savedProduct.getProductName()).isEqualTo("제품1");
        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(1000);
    }


    @Transactional
    @Test
    public void update() throws Exception{
        //given
        String url = baseUrl + "/";
        ProductSaveRequestDto saveDto = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1")
                .price(1000L)
                .stock(100L)
                .rateDiscount(10)
                .build();
        Long saveId = productService.save(saveDto);
        ProductUpdateRequestDto requestDto = ProductUpdateRequestDto.builder()
                .price(2000L)
                .stock(150L)
                .productName("변경된제품1")
                .build();

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.put(url + String.valueOf(saveId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        Product savedProduct = productRepository.findById(saveId).orElseThrow(() -> new RuntimeException("잘못된 저장된 product id"));
        Assertions.assertThat(savedProduct.getProductName()).isEqualTo("변경된제품1");
        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(2000);
        Assertions.assertThat(savedProduct.getStock()).isEqualTo(150);
    }

    @Transactional
    @Test
    public void delete() throws Exception{
        //given
        String url = baseUrl + "/";
        ProductSaveRequestDto saveDto = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1")
                .price(1000L)
                .stock(100L)
                .rateDiscount(10)
                .build();
        Long saveId = productService.save(saveDto);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete(url + saveId));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> productRepository.findById(saveId).orElseThrow(() ->new NoSuchElementException("Fail")));
    }

    @Transactional
    @Test
    public void findById() throws Exception{
        //given
        String url = baseUrl + "/";
        ProductSaveRequestDto saveDto = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1")
                .price(1000L)
                .stock(100L)
                .rateDiscount(10)
                .build();
        Long saveId = productService.save(saveDto);


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url + String.valueOf(saveId)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(saveId));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(saveItemId1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("제품1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1000));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.stock").value(100));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.rateDiscount").value(10));
    }

    @Transactional
    @Test
    public void findByItemIdWithNoProductCondition() throws Exception{
        //given
        String url = baseUrl + "/products";
        ProductSaveRequestDto saveDto1_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-1")
                .price(1100L)
                .stock(110L)
                .build();
        ProductSaveRequestDto saveDto1_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-2")
                .price(1200L)
                .stock(120L)
                .build();
        ProductSaveRequestDto saveDto1_3 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-3")
                .price(1300L)
                .stock(130L)
                .build();
        ProductSaveRequestDto saveDto1_4 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-4")
                .price(1400L)
                .stock(140L)
                .build();
        ProductSaveRequestDto saveDto2_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-1")
                .price(2100L)
                .stock(210L)
                .build();
        ProductSaveRequestDto saveDto2_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-2")
                .price(2200L)
                .stock(220L)
                .build();
        Long saveId1_1 = productService.save(saveDto1_1);
        Long saveId1_2 = productService.save(saveDto1_2);
        Long saveId1_3 = productService.save(saveDto1_3);
        Long saveId1_4 = productService.save(saveDto1_4);
        Long saveId2_1 = productService.save(saveDto2_1);
        Long saveId2_2 = productService.save(saveDto2_2);


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("itemId", String.valueOf(saveItemId1))
                .queryParam("page", "0")
                .queryParam("size", "3"));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(4));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.productNum").value(3));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].id").value(saveId1_1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].id").value(saveId1_2));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[2].id").value(saveId1_3));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].productName").value("제품1-1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].productName").value("제품1-2"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[2].productName").value("제품1-3"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].price").value(1100));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].price").value(1200));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[2].price").value(1300));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].itemId").value(saveItemId1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].itemName").value("침대1"));
    }
    @Transactional
    @Test
    public void findByItemIdWithProductCondition1() throws Exception{
        //given
        String url = baseUrl + "/products";
        ProductSaveRequestDto saveDto1_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-1")
                .price(1100L)
                .stock(110L)
                .build();
        ProductSaveRequestDto saveDto1_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-2")
                .price(1200L)
                .stock(120L)
                .build();
        ProductSaveRequestDto saveDto1_3 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-3")
                .price(1300L)
                .stock(130L)
                .build();
        ProductSaveRequestDto saveDto1_4 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-4")
                .price(1400L)
                .stock(140L)
                .build();
        ProductSaveRequestDto saveDto2_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-1")
                .price(2100L)
                .stock(210L)
                .build();
        ProductSaveRequestDto saveDto2_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-2")
                .price(2200L)
                .stock(220L)
                .build();
        Long saveId1_1 = productService.save(saveDto1_1);
        Long saveId1_2 = productService.save(saveDto1_2);
        Long saveId1_3 = productService.save(saveDto1_3);
        Long saveId1_4 = productService.save(saveDto1_4);
        Long saveId2_1 = productService.save(saveDto2_1);
        Long saveId2_2 = productService.save(saveDto2_2);


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("itemId", String.valueOf(saveItemId1))
                .queryParam("page", "0")
                .queryParam("size", "3")
                .queryParam("productName", "제품1-2"));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.productNum").value(1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].id").value(saveId1_2));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].productName").value("제품1-2"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].price").value(1200));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].itemId").value(saveItemId1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].itemName").value("침대1"));
    }
    @Transactional
    @Test
    public void findByItemIdWithProductCondition2() throws Exception{
        //given
        String url = baseUrl + "/products";
        ProductSaveRequestDto saveDto1_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-1")
                .price(1100L)
                .stock(110L)
                .build();
        ProductSaveRequestDto saveDto1_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-2")
                .price(1200L)
                .stock(120L)
                .build();
        ProductSaveRequestDto saveDto1_3 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-3")
                .price(1300L)
                .stock(130L)
                .build();
        ProductSaveRequestDto saveDto1_4 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-4")
                .price(1400L)
                .stock(140L)
                .build();
        ProductSaveRequestDto saveDto2_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-1")
                .price(2100L)
                .stock(210L)
                .build();
        ProductSaveRequestDto saveDto2_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-2")
                .price(2200L)
                .stock(220L)
                .build();
        Long saveId1_1 = productService.save(saveDto1_1);
        Long saveId1_2 = productService.save(saveDto1_2);
        Long saveId1_3 = productService.save(saveDto1_3);
        Long saveId1_4 = productService.save(saveDto1_4);
        Long saveId2_1 = productService.save(saveDto2_1);
        Long saveId2_2 = productService.save(saveDto2_2);


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("itemId", String.valueOf(saveItemId1))
                .queryParam("page", "0")
                .queryParam("size", "3")
                .queryParam("productName", "제품1-2")
                .queryParam("priceBegin", "3000"));


        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(0));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.productNum").value(0));
    }
    @Transactional
    @Test
    public void findByItemIdWithProductCondition3() throws Exception{
        //given
        String url = baseUrl + "/products";
        ProductSaveRequestDto saveDto1_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-1")
                .price(1100L)
                .stock(110L)
                .build();
        ProductSaveRequestDto saveDto1_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-2")
                .price(1200L)
                .stock(120L)
                .build();
        ProductSaveRequestDto saveDto1_3 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-3")
                .price(1300L)
                .stock(130L)
                .build();
        ProductSaveRequestDto saveDto1_4 = ProductSaveRequestDto.builder()
                .itemId(saveItemId1)
                .productName("제품1-4")
                .price(1400L)
                .stock(140L)
                .build();
        ProductSaveRequestDto saveDto2_1 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-1")
                .price(2100L)
                .stock(210L)
                .build();
        ProductSaveRequestDto saveDto2_2 = ProductSaveRequestDto.builder()
                .itemId(saveItemId2)
                .productName("제품2-2")
                .price(2200L)
                .stock(220L)
                .build();
        Long saveId1_1 = productService.save(saveDto1_1);
        Long saveId1_2 = productService.save(saveDto1_2);
        Long saveId1_3 = productService.save(saveDto1_3);
        Long saveId1_4 = productService.save(saveDto1_4);
        Long saveId2_1 = productService.save(saveDto2_1);
        Long saveId2_2 = productService.save(saveDto2_2);


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                .queryParam("itemId", String.valueOf(saveItemId1))
                .queryParam("page", "0")
                .queryParam("size", "3")
                .queryParam("priceBegin", "1300")
                .queryParam("priceEnd", "1400"));


        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(2));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.productNum").value(2));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].id").value(saveId1_3));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].id").value(saveId1_4));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].productName").value("제품1-3"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].productName").value("제품1-4"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].price").value(1300));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[1].price").value(1400));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].itemId").value(saveItemId1));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.products[0].itemName").value("침대1"));
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