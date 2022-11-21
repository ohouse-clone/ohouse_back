package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.ItemCategoryRepository;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.category.ItemCategory;
import com.clone.ohouse.store.domain.item.bed.dto.BedRequestDto;
import com.clone.ohouse.store.domain.item.dto.ItemBundleViewDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        CategorySearch condition = new CategorySearch(20L, 22L, 20L, 20L);
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
        CategorySearch condition = new CategorySearch(20L, 22L, 20L, 20L);
        String name = "나무침대";
        BedSize size = BedSize.MS;
        BedColor color = BedColor.WHITE;
        Bed bed = new Bed(name, "JJH1", "JHCOM", size, color);
        Long savedId = itemService.save(bed, condition);

        //when
        Item savedItem = itemRepository.findById(savedId).get();
        itemService.delete(savedItem.getId());

        //then
        Assertions.assertThat(itemRepository.count()).isEqualTo(0);
        Assertions.assertThat(itemCategoryRepository.count()).isEqualTo(0);
    }

    @Test
    void findByCategory() throws Exception {
        //given
        CategorySearch condition1 = new CategorySearch(20L, 22L, 20L, 20L);
        CategorySearch condition2 = new CategorySearch(20L, 22L, 20L, 21L);
        CategorySearch condition3 = new CategorySearch(20L, 22L, 20L, 22L);
        itemService.save(new Bed("침대1", "모델1", "브랜드1", BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대2", "모델2", "브랜드2", BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대3", "모델3", "브랜드3", BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new Bed("침대4", "모델4", "브랜드4", BedSize.K, BedColor.BLUE), condition1);
        itemService.save(new StorageBed("수납침대1", "수납모델1", "수납브랜드1", Material.FAKE_LEATHER ), condition2);
        itemService.save(new StorageBed("수납침대2", "수납모델2", "수납브랜드2", Material.FAKE_LEATHER ), condition2);


        //when
        Pageable pageable = PageRequest.of(0, 3);
        ItemBundleViewDto dto = itemService.findByCategory(condition1, pageable);

        //then
        Assertions.assertThat(dto.getTotalNum()).isEqualTo(4L);
        Assertions.assertThat(dto.getItemNum()).isEqualTo(3L);
        Assertions.assertThat(dto.getItems())
                .map((irdto) -> (BedRequestDto)irdto)
                .extracting(BedRequestDto::getName)
                .containsExactly("침대1", "침대2", "침대3");
    }

}