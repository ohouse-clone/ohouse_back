package com.clone.ohouse.shop.product.domain.access;


import com.clone.ohouse.shop.product.domain.entity.Bed;
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
public class BedRepositoryTest {
    @Autowired
    private BedRepository bedRepository;

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
        bedRepository.deleteAll();
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
        Bed savedItem = bedRepository.save(Bed.builder()
                .categoryCode(one.orElseThrow(()->new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());

        //when
        List<Bed> list = bedRepository.findAll();

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
        Bed savedItem = bedRepository.save(Bed.builder()
                .categoryCode(one.orElseThrow(()->new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());

        //when
        bedRepository.delete(savedItem);

        List<Bed> all = bedRepository.findAll();

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
        Bed savedItem = bedRepository.save(Bed.builder()
                .categoryCode(one.orElseThrow(() -> new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());
        //when
        List<Bed> all = bedRepository.findAll();
        Bed item = all.get(0);
        item.update(null,"한샘침대",null,null,null,null);
        bedRepository.save(item);

        Optional<Bed> itemForCompare = bedRepository.findById(savedItem.getId());

        //then
        Assertions.assertThat(itemForCompare.orElseThrow(()->new Exception("Fail to find 등록된 한샘침대")).getName()).isEqualTo(item.getName());
    }
}
