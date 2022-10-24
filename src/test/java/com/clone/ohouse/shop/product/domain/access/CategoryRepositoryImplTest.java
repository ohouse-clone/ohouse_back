package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.Category;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CategoryRepositoryImplTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void beforeEach(){
        Category c1 = new Category("가구",20L);
        Category c2 = new Category("침대",22L);
        c2.registerParent(c1);
        Category c3 = new Category("침대프레임",20L);
        c3.registerParent(c2);
        Category c4 = new Category("일반침대",19L);
        c4.registerParent(c3);
        Category c5 = new Category("수납침대",18L);
        c5.registerParent(c3);
        Category c6 = new Category("저상형침대",17L);
        c6.registerParent(c3);
        Category c7 = new Category("침대+메트릭스",21L);
        c7.registerParent(c2);
        Category c8 = new Category("일반침대",20L);
        c8.registerParent(c7);
        Category c9 = new Category("수납침대",21L);
        c9.registerParent(c7);
        Category c10 = new Category("저상형침대",22L);
        c10.registerParent(c7);
        Category c11 = new Category("매트릭스토퍼",23L);
        c11.registerParent(c2);
        Category c12 = new Category("매트리스",20L);
        c12.registerParent(c11);
        Category c13 = new Category("토퍼",21L);
        c13.registerParent(c12);
        Category c14 = new Category("스프링매트리스",20L);
        c14.registerParent(c12);
        Category c15 = new Category("라텍스매트리스",21L);
        c14.registerParent(c12);

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
    }
    @AfterEach
    public void clean(){
        categoryRepository.deleteAll();
    }


    @Test
    void findCategoryWithSingleCondition(){
        CategorySearch condition = new CategorySearch(20L, null, null, null);
        Category findCategory = categoryRepository.findCategory(condition);

        Assertions.assertThat(findCategory.getCode()).isEqualTo(20L);
        Assertions.assertThat(findCategory.getName()).isEqualTo("가구");
    }

    @Test
    void findCategoryWithSDoubleCondition(){
        CategorySearch condition = new CategorySearch(20L, 22L, null, null);
        Category findCategory = categoryRepository.findCategory(condition);

        Assertions.assertThat(findCategory.getCode()).isEqualTo(22L);
        Assertions.assertThat(findCategory.getName()).isEqualTo("침대");
    }

    @Test
    void findCategoryWithThreeCondition(){
        CategorySearch condition = new CategorySearch(20L, 22L, 21L, null);
        Category findCategory = categoryRepository.findCategory(condition);

        Assertions.assertThat(findCategory.getCode()).isEqualTo(21L);
        Assertions.assertThat(findCategory.getName()).isEqualTo("침대+메트릭스");
    }

    @Test
    void findCategoryWithFourCondition(){
        CategorySearch condition = new CategorySearch(20L, 22L, 20L, 17L);
        Category findCategory = categoryRepository.findCategory(condition);

        Assertions.assertThat(findCategory.getCode()).isEqualTo(17L);
        Assertions.assertThat(findCategory.getName()).isEqualTo("저상형침대");
    }

}