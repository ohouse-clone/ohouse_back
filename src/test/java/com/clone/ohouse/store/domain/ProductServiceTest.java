package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.*;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSize;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.dto.ProductDto;
import com.clone.ohouse.store.domain.product.dto.ProductSaveRequestDto;
import com.clone.ohouse.store.domain.product.dto.ProductUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceTest {

    @Autowired ProductService productService;
    @Autowired ItemService itemService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemCategoryRepository itemCategoryRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired ProductRepository productRepository;

    Long saveItemId1 = null;
    Long saveItemId2 = null;
    Long saveItemId3 = null;
    @BeforeEach
    void setup() throws Exception{
        //category setup
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

        //item setup
        CategorySearch categorySearch = CategoryParser.parseCategoryString("20_22_20_20");
        saveItemId1 = itemService.save(new Bed("침대1","모델1","브1", BedSize.CK, BedColor.RED), categorySearch);
        saveItemId2 = itemService.save(new Bed("침대2","모델2","브2", BedSize.CK, BedColor.RED), categorySearch);
        saveItemId3 = itemService.save(new Bed("침대3","모델3","브3", BedSize.CK, BedColor.RED), categorySearch);

    }
    @AfterEach
    void clean(){
        itemCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        itemRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void save() throws Exception{
        //given
        ProductSaveRequestDto dto1 = new ProductSaveRequestDto(
                saveItemId1,
                "제품1",
                1000,
                100,
                50);
        //when
        Long saveProductId1 = productService.save(dto1);


        //then
        Product findProduct = productRepository.findById(saveProductId1).orElseThrow(() -> new NoSuchElementException("잘못된 product id입니다."));
        Item findItem1 = itemRepository.findById(saveItemId1).orElseThrow(() -> new RuntimeException("잘못 저장된 item"));
        Assertions.assertThat(findProduct.getProductName()).isEqualTo("제품1");
        Assertions.assertThat(findProduct.getPrice()).isEqualTo(1000);
        Assertions.assertThat(findProduct.getItem()).isEqualTo(findItem1);
    }

    @Test
    void update() throws Exception{
        //given
        ProductSaveRequestDto dto1 = new ProductSaveRequestDto(
                saveItemId1,
                "제품1",
                1000,
                100,
                50);
        Long saveProductId1 = productService.save(dto1);
        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto(saveItemId2, "바뀐제품1", 2000, 50, 0);

        //when
        productService.update(saveProductId1, requestDto);


        //then
        Product findProduct = productRepository.findById(saveProductId1).orElseThrow(() -> new NoSuchElementException("잘못된 product id입니다."));
        Item findItem2 = itemRepository.findById(saveItemId2).orElseThrow(() -> new RuntimeException("잘못 저장된 item"));
        Assertions.assertThat(findProduct.getProductName()).isEqualTo("바뀐제품1");
        Assertions.assertThat(findProduct.getPrice()).isEqualTo(2000);
        Assertions.assertThat(findProduct.getItem()).isEqualTo(findItem2);
    }
    @Test
    void delete() throws Exception{
        //given
        ProductSaveRequestDto dto1 = new ProductSaveRequestDto(
                saveItemId1,
                "제품1",
                1000,
                100,
                50);
        Long saveProductId1 = productService.save(dto1);

        //when
        productService.delete(saveProductId1);

        //then
        Assertions.assertThat(productRepository.count()).isEqualTo(0);
    }
    @Test
    void findByIdWithFetch() throws Exception {
        //given
        ProductSaveRequestDto dto1 = new ProductSaveRequestDto(
                saveItemId1,
                "제품1",
                1000,
                100,
                50);
        Long saveProductId1 = productService.save(dto1);

        //when
        ProductDto result = productService.findByIdWithFetch(saveProductId1);

        //then
        Assertions.assertThat(result.getId()).isEqualTo(saveProductId1);
        Assertions.assertThat(result.getProductName()).isEqualTo("제품1");
        Assertions.assertThat(result.getPrice()).isEqualTo(1000);
        Assertions.assertThat(result.getItemId()).isEqualTo(saveItemId1);
    }
}