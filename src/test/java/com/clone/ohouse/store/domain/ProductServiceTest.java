package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.*;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSize;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.ProductSearchCondition;
import com.clone.ohouse.store.domain.product.dto.*;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostWithProductsDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired ProductService productService;
    @Autowired ItemService itemService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemCategoryRepository itemCategoryRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired ProductRepository productRepository;
    @Autowired StorePostsRepository storePostsRepository;
    @Autowired StorePostsService storePostsService;

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
        storePostsRepository.deleteAll();
    }

    @Test
    void save() throws Exception{
        //given
        ProductSaveRequestDto dto1 = new ProductSaveRequestDto(
                saveItemId1,
                "제품1",
                1000,
                100,
                50,
                null);
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
                50,
                null);
        Long saveProductId1 = productService.save(dto1);
        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto(saveItemId2, "바뀐제품1", 2000, 50, 0, null);

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
                50,
                null);
        Long saveProductId1 = productService.save(dto1);

        //when
        productService.delete(saveProductId1);

        //then
        Assertions.assertThat(productRepository.count()).isEqualTo(0);
    }


    @Transactional
    @Test
    void findByIdWithFetchJoin() throws Exception {
        //given
        ProductSaveRequestDto dto1 = new ProductSaveRequestDto(
                saveItemId1,
                "제품1",
                1000,
                100,
                50,
                null);
        Long saveProductId1 = productService.save(dto1);


        //when
        ProductDetailDto result = productService.findByIdWithFetchJoin(saveProductId1);


        //then
        Assertions.assertThat(result.getId()).isEqualTo(saveProductId1);
        Assertions.assertThat(result.getProductName()).isEqualTo("제품1");
        Assertions.assertThat(result.getPrice()).isEqualTo(1000);
        Assertions.assertThat(result.getItemId()).isEqualTo(saveItemId1);
    }

    @Transactional
    @Test
    void findByItem() throws Exception{
        //given
        ProductSaveRequestDto dto1_1 = new ProductSaveRequestDto(saveItemId1, "제품1-1", 1100, 110, 50, null);
        ProductSaveRequestDto dto1_2 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1200, 120, 55, null);
        ProductSaveRequestDto dto1_3 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1300, 130, 55, null);
        ProductSaveRequestDto dto2_1 = new ProductSaveRequestDto(saveItemId2, "제품2-1", 2100, 210, 55, null);
        ProductSaveRequestDto dto2_2 = new ProductSaveRequestDto(saveItemId2, "제품2-2", 2200, 220, 55, null);
        ProductSaveRequestDto dto2_3 = new ProductSaveRequestDto(saveItemId2, "제품2-3", 2300, 230, 55, null);
        Long saveProductId1_1 = productService.save(dto1_1);
        Long saveProductId1_2 = productService.save(dto1_2);
        Long saveProductId1_3 = productService.save(dto1_3);
        Long saveProductId2_1 = productService.save(dto2_1);
        Long saveProductId2_2 = productService.save(dto2_2);
        Long saveProductId2_3 = productService.save(dto2_3);
        ProductSearchCondition productSearchCondition = new ProductSearchCondition();
        //when
        ProductListResponseDto result = productService.findByItemWithProductCondition(PageRequest.of(0, 2), saveItemId2, productSearchCondition);


        //then
        Assertions.assertThat(result.getTotalNum()).isEqualTo(3);
        Assertions.assertThat(result.getProductNum()).isEqualTo(2);
        Assertions.assertThat(result.getProducts()).extracting(ProductResponseDto::getProductName)
                .containsExactly("제품2-1", "제품2-2");
    }
    @Transactional
    @Test
    void findByItemWithProductCondition() throws Exception{
        //given
        ProductSaveRequestDto dto1_1 = new ProductSaveRequestDto(saveItemId1, "제품1-1", 1100, 110, 50, null);
        ProductSaveRequestDto dto1_2 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1200, 120, 55, null);
        ProductSaveRequestDto dto1_3 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1300, 130, 55, null);
        ProductSaveRequestDto dto2_1 = new ProductSaveRequestDto(saveItemId2, "제품2-1", 2100, 210, 55, null);
        ProductSaveRequestDto dto2_2 = new ProductSaveRequestDto(saveItemId2, "제품2-2", 2200, 220, 55, null);
        ProductSaveRequestDto dto2_3 = new ProductSaveRequestDto(saveItemId2, "제품2-3", 2300, 230, 55, null);
        Long saveProductId1_1 = productService.save(dto1_1);
        Long saveProductId1_2 = productService.save(dto1_2);
        Long saveProductId1_3 = productService.save(dto1_3);
        Long saveProductId2_1 = productService.save(dto2_1);
        Long saveProductId2_2 = productService.save(dto2_2);
        Long saveProductId2_3 = productService.save(dto2_3);
        ProductSearchCondition productSearchCondition = new ProductSearchCondition();
        productSearchCondition.setProductName("제품2-3");
        //when
        ProductListResponseDto result = productService.findByItemWithProductCondition(PageRequest.of(0, 2), saveItemId2, productSearchCondition);


        //then
        Assertions.assertThat(result.getTotalNum()).isEqualTo(1);
        Assertions.assertThat(result.getProductNum()).isEqualTo(1);
        Assertions.assertThat(result.getProducts()).extracting(ProductResponseDto::getProductName)
                .containsExactly("제품2-3");
    }

    @Transactional
    @Test
    void findByItemWithProductConditionWithConditionDetail1() throws Exception{
        //given
        ProductSaveRequestDto dto1_1 = new ProductSaveRequestDto(saveItemId1, "제품1-1", 1100, 110, 50, null);
        ProductSaveRequestDto dto1_2 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1200, 120, 55, null);
        ProductSaveRequestDto dto1_3 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1300, 130, 55, null);
        ProductSaveRequestDto dto2_1 = new ProductSaveRequestDto(saveItemId2, "제품2-1", 2100, 210, 55, null);
        ProductSaveRequestDto dto2_2 = new ProductSaveRequestDto(saveItemId2, "제품2-2", 2200, 220, 55, null);
        ProductSaveRequestDto dto2_3 = new ProductSaveRequestDto(saveItemId2, "제품2-3", 2300, 230, 55, null);
        Long saveProductId1_1 = productService.save(dto1_1);
        Long saveProductId1_2 = productService.save(dto1_2);
        Long saveProductId1_3 = productService.save(dto1_3);
        Long saveProductId2_1 = productService.save(dto2_1);
        Long saveProductId2_2 = productService.save(dto2_2);
        Long saveProductId2_3 = productService.save(dto2_3);
        ProductSearchCondition productSearchCondition = new ProductSearchCondition();
        productSearchCondition.setPriceBegin(1000);
        productSearchCondition.setPriceEnd(1200);
        //when
        ProductListResponseDto result = productService.findByItemWithProductCondition(PageRequest.of(0, 3), saveItemId1, productSearchCondition);


        //then
        Assertions.assertThat(result.getTotalNum()).isEqualTo(2);
        Assertions.assertThat(result.getProductNum()).isEqualTo(2);
        Assertions.assertThat(result.getProducts()).extracting(ProductResponseDto::getProductName)
                .containsExactly("제품1-1", "제품1-2");
    }

    @Transactional
    @Test
    void findByItemWithProductConditionWithConditionDetail2() throws Exception{
        //given
        ProductSaveRequestDto dto1_1 = new ProductSaveRequestDto(saveItemId1, "제품1-1", 1100, 110, 50, null);
        ProductSaveRequestDto dto1_2 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1200, 120, 55, null);
        ProductSaveRequestDto dto1_3 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1300, 130, 55, null);
        ProductSaveRequestDto dto2_1 = new ProductSaveRequestDto(saveItemId2, "제품2-1", 2100, 210, 55, null);
        ProductSaveRequestDto dto2_2 = new ProductSaveRequestDto(saveItemId2, "제품2-2", 2200, 220, 55, null);
        ProductSaveRequestDto dto2_3 = new ProductSaveRequestDto(saveItemId2, "제품2-3", 2300, 230, 55, null);
        Long saveProductId1_1 = productService.save(dto1_1);
        Long saveProductId1_2 = productService.save(dto1_2);
        Long saveProductId1_3 = productService.save(dto1_3);
        Long saveProductId2_1 = productService.save(dto2_1);
        Long saveProductId2_2 = productService.save(dto2_2);
        Long saveProductId2_3 = productService.save(dto2_3);
        ProductSearchCondition productSearchCondition = new ProductSearchCondition();
        productSearchCondition.setStockBegin(220);

        //when
        ProductListResponseDto result = productService.findByItemWithProductCondition(PageRequest.of(0, 3), saveItemId2, productSearchCondition);


        //then
        Assertions.assertThat(result.getTotalNum()).isEqualTo(2);
        Assertions.assertThat(result.getProductNum()).isEqualTo(2);
        Assertions.assertThat(result.getProducts()).extracting(ProductResponseDto::getProductName)
                .containsExactly("제품2-2", "제품2-3");
    }
    @Transactional
    @Test
    void findByItemWithProductConditionWithConditionDetail3() throws Exception{
        //given
        ProductSaveRequestDto dto1_1 = new ProductSaveRequestDto(saveItemId1, "제품1-1", 1100, 110, 50, null);
        ProductSaveRequestDto dto1_2 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1200, 120, 55, null);
        ProductSaveRequestDto dto1_3 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1300, 130, 55, null);
        ProductSaveRequestDto dto2_1 = new ProductSaveRequestDto(saveItemId2, "제품2-1", 2100, 210, 55, null);
        ProductSaveRequestDto dto2_2 = new ProductSaveRequestDto(saveItemId2, "제품2-2", 2200, 220, 55, null);
        ProductSaveRequestDto dto2_3 = new ProductSaveRequestDto(saveItemId2, "제품2-3", 2300, 230, 55, null);
        Long saveProductId1_1 = productService.save(dto1_1);
        Long saveProductId1_2 = productService.save(dto1_2);
        Long saveProductId1_3 = productService.save(dto1_3);
        Long saveProductId2_1 = productService.save(dto2_1);
        Long saveProductId2_2 = productService.save(dto2_2);
        Long saveProductId2_3 = productService.save(dto2_3);
        ProductSearchCondition productSearchCondition = new ProductSearchCondition();
        productSearchCondition.setStockBegin(220);
        productSearchCondition.setProductName("제품2-1");

        //when
        ProductListResponseDto result = productService.findByItemWithProductCondition(PageRequest.of(0, 3), saveItemId2, productSearchCondition);


        //then
        Assertions.assertThat(result.getTotalNum()).isEqualTo(0);
        Assertions.assertThat(result.getProductNum()).isEqualTo(0);
    }

    @Test
    void updateWithStorePostId() throws Exception{
        //given
        ProductSaveRequestDto dto1_1 = new ProductSaveRequestDto(saveItemId1, "제품1-1", 1100, 110, 50, null);
        ProductSaveRequestDto dto1_2 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1200, 120, 55, null);
        ProductSaveRequestDto dto1_3 = new ProductSaveRequestDto(saveItemId1, "제품1-2", 1300, 130, 55, null);
        ProductSaveRequestDto dto2_1 = new ProductSaveRequestDto(saveItemId2, "제품2-1", 2100, 210, 55, null);
        ProductSaveRequestDto dto2_2 = new ProductSaveRequestDto(saveItemId2, "제품2-2", 2200, 220, 55, null);
        ProductSaveRequestDto dto2_3 = new ProductSaveRequestDto(saveItemId2, "제품2-3", 2300, 230, 55, null);
        Long saveProductId1_1 = productService.save(dto1_1);
        Long saveProductId1_2 = productService.save(dto1_2);
        Long saveProductId1_3 = productService.save(dto1_3);
        Long saveProductId2_1 = productService.save(dto2_1);
        Long saveProductId2_2 = productService.save(dto2_2);
        Long saveProductId2_3 = productService.save(dto2_3);
        StorePostsSaveRequestDto requestDto1 = new StorePostsSaveRequestDto("제목1", null, null, "jjh1");
        StorePostsSaveRequestDto requestDto2 = new StorePostsSaveRequestDto("제목2", null, null, "jjh2");
        Long savePostId1 = storePostsService.save(requestDto1);
        Long savePostId2 = storePostsService.save(requestDto2);

        //when
        productService.updateWithStorePostId(new ProductStorePostIdUpdateRequestDto(savePostId1, new ArrayList<>(List.of(saveProductId1_2, saveProductId1_3, saveProductId2_1))));

        //then
        StorePostWithProductsDto result = storePostsService.findByIdWithProduct(savePostId1);
        Assertions.assertThat(result.getAuthor()).isEqualTo("jjh1");
        Assertions.assertThat(result.getTitle()).isEqualTo("제목1");
        Assertions.assertThat(result.getProductNum()).isEqualTo(3);
        Assertions.assertThat(result.getProducts().get(0).getId()).isEqualTo(saveProductId1_2);
        Assertions.assertThat(result.getProducts().get(1).getId()).isEqualTo(saveProductId1_3);
        Assertions.assertThat(result.getProducts().get(2).getId()).isEqualTo(saveProductId2_1);
        Assertions.assertThat(result.getProducts().get(0).getProductName()).isEqualTo("제품1-2");
    }
}