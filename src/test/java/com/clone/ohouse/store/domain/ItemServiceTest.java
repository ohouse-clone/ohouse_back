package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.ItemCategoryRepository;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.category.ItemCategory;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSize;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemServiceTest {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemCategoryRepository itemCategoryRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @BeforeEach
    void setup(){
        Category c1 = new Category("가구",20L);
        Category c2 = new Category("침대",22L);
        c2.addParent(c1);
        Category c3 = new Category("침대프레임",20L);
        c3.addParent(c2);
        Category c4 = new Category("일반침대",19L);
        c4.addParent(c3);
        Category c5 = new Category("수납침대",18L);
        c5.addParent(c3);
        Category c6 = new Category("저상형침대",17L);
        c6.addParent(c3);
        Category c7 = new Category("침대+메트릭스",21L);
        c7.addParent(c2);
        Category c8 = new Category("일반침대",20L);
        c8.addParent(c7);
        Category c9 = new Category("수납침대",21L);
        c9.addParent(c7);
        Category c10 = new Category("저상형침대",22L);
        c10.addParent(c7);
        Category c11 = new Category("매트릭스토퍼",23L);
        c11.addParent(c2);
        Category c12 = new Category("매트리스",20L);
        c12.addParent(c11);
        Category c13 = new Category("토퍼",21L);
        c13.addParent(c12);
        Category c14 = new Category("스프링매트리스",20L);
        c14.addParent(c12);
        Category c15 = new Category("라텍스매트리스",21L);
        c14.addParent(c12);

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
    void clean(){
        itemCategoryRepository.deleteAll();
        itemRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void save() throws Exception{
        //given
        CategorySearch condition = new CategorySearch(20L, 22L, 20L, 17L);
        String name = "나무침대";
        BedSize size = BedSize.CK;
        BedColor color = BedColor.WHITE;
        Bed bed = new Bed(name, "JJH1", "JHCOM", size, color);

        //when
        Long savedId = itemService.save(bed, condition);

        //then
        Item savedItem = itemRepository.findById(savedId).get();

        List<ItemCategory> byItem = itemCategoryRepository.findByItem(savedItem);

        for (ItemCategory itemCategory : byItem)
            Assertions.assertThat(savedItem.getItemCategories()).extracting(ItemCategory::getId).contains(itemCategory.getId());
        Assertions.assertThat(savedItem.getItemCategories().size()).isEqualTo(Category.CATEGORY_SIZE);
        Assertions.assertThat(savedItem.getName()).isEqualTo(name);
        Assertions.assertThat(((Bed)savedItem).getSize()).isEqualTo(size);
    }

    @Test
    void delete() throws Exception {
        //given
        CategorySearch condition = new CategorySearch(20L, 22L, 20L, 17L);
        String name = "나무침대";
        BedSize size = BedSize.MS;
        BedColor color = BedColor.WHITE;
        Bed bed = new Bed(name, "JJH1", "JHCOM", size, color);
        Long savedId = itemService.save(bed, condition);

        //when
        Item savedItem = itemRepository.findById(savedId).get();
        itemService.delete(savedItem);

        //then
        Assertions.assertThat(itemRepository.count()).isEqualTo(0);
        Assertions.assertThat(itemCategoryRepository.count()).isEqualTo(0);
    }

}