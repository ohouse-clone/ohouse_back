package com.clone.ohouse.shop.product.domain.access;


import com.clone.ohouse.shop.product.domain.entity.Furniture;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FurnitureRepositoryTest {
    @Autowired
    private FurnitureRepository furnitureRepository;

    @Autowired
    private ItemCategoryCodeRepository itemCategoryCodeRepository;
    @BeforeEach
    public void previouslySaveItemCategoryCode(){

        ItemCategoryCode code = ItemCategoryCode.builder()
                .categoryDetail("가구_침대_침대프레임_일반침대")
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("20")
                .build();
        itemCategoryCodeRepository.save(code);
    }
    @AfterEach
    public void cleanUp() {
        furnitureRepository.deleteAll();
        itemCategoryCodeRepository.deleteAll();
    }

    @Test
    @DisplayName("ItemRepository 저장")
    public void itemRepositorySave() throws Exception{
        //given
        ItemCategoryCode categoryCode = ItemCategoryCode.builder()
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("20")
                .build();
        Example<ItemCategoryCode> e = Example.of(categoryCode);
        Optional<ItemCategoryCode> one = itemCategoryCodeRepository.findOne(e);

        String itemName = "시몬스침대";
        Item savedItem = furnitureRepository.save(Furniture.builder()
                .categoryCode(one.orElseThrow(()->new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());

        //when
        List<Furniture> list = furnitureRepository.findAll();

        //then
        Item item = list.get(0);

        Assertions.assertThat(item.getCategoryCode().getCategory4()).isEqualTo("20");
        Assertions.assertThat(item.getName()).isEqualTo(savedItem.getName());
    }

    @Test
    @DisplayName("ItemRepository 삭제")
    public void itemRepositoryDelete() throws Exception{
        //given
        ItemCategoryCode categoryCode = ItemCategoryCode.builder()
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("20")
                .build();
        Example<ItemCategoryCode> e = Example.of(categoryCode);
        Optional<ItemCategoryCode> one = itemCategoryCodeRepository.findOne(e);

        String itemName = "이케아침대";
        Furniture savedItem = furnitureRepository.save(Furniture.builder()
                .categoryCode(one.orElseThrow(()->new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());

        //when
        furnitureRepository.delete(savedItem);

        List<Furniture> all = furnitureRepository.findAll();

        //then
        Assertions.assertThat(all.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("ItemRepository 수정")
    public void itemRepositoryUpdate() throws Exception{
        //given
        ItemCategoryCode categoryCode = ItemCategoryCode.builder()
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("20")
                .build();
        Example<ItemCategoryCode> e = Example.of(categoryCode);
        Optional<ItemCategoryCode> one = itemCategoryCodeRepository.findOne(e);

        String itemName = "이케아침대";
        Furniture savedItem = furnitureRepository.save(Furniture.builder()
                .categoryCode(one.orElseThrow(() -> new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());
        //when
        List<Furniture> all = furnitureRepository.findAll();
        Furniture item = all.get(0);
        item.update(null,"한샘침대",null,null,null,null);
        furnitureRepository.save(item);

        Optional<Furniture> itemForCompare = furnitureRepository.findById(savedItem.getId());

        //then
        Assertions.assertThat(itemForCompare.orElseThrow(()->new Exception("Fail to find 등록된 한샘침대")).getName()).isEqualTo(item.getName());
    }
}
