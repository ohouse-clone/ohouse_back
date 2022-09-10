package com.clone.ohouse.shop.product.domain.access;


import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
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

import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;


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
                .category1(1002)
                .category2(1002)
                .category3(1003)
                .category4(1004)
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
                .category1(1002)
                .category2(1002)
                .category3(1003)
                .category4(1004)
                .build();
        Example<ItemCategoryCode> e = Example.of(categoryCode);
        Optional<ItemCategoryCode> one = itemCategoryCodeRepository.findOne(e);

        String itemName = "시몬스침대";
        ItemCategoryCode code = one.orElseThrow(() -> new Exception("가구_침대_침대프레임_일반침대 항목 없음"));
        Bed savedItem = bedRepository.save(Bed.builder()
                .categoryCode(code)
                .name(itemName)
                .build());

        //when
        List<Bed> list = bedRepository.findAll();

        //then

        Assertions.assertThat(list).extracting(Bed::getName)
                .containsExactly(itemName);
        Assertions.assertThat(list).extracting(Bed::getCategoryCode)
                        .extracting(ItemCategoryCode::getCategory1, ItemCategoryCode::getCategory2, ItemCategoryCode::getCategory3, ItemCategoryCode::getCategory4)
                                .containsExactly(Tuple.tuple(code.getCategory1(), code.getCategory2(), code.getCategory3(), code.getCategory4()));
        System.out.println("id : " + savedItem.getId());
        System.out.println("wd : " + savedItem.getName());

        System.out.println("l id : " + list.get(0).getId());
        System.out.println("l wd : " + list.get(0).getName());

    }

    @Test
    @DisplayName("ItemRepository 삭제")
    public void itemRepositoryDelete() throws Exception{
        //given
        ItemCategoryCode categoryCode = ItemCategoryCode.builder()
                .category1(1002)
                .category2(1002)
                .category3(1003)
                .category4(1004)
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
                .category1(1002)
                .category2(1002)
                .category3(1003)
                .category4(1004)
                .build();
        Example<ItemCategoryCode> e = Example.of(categoryCode);
        Optional<ItemCategoryCode> one = itemCategoryCodeRepository.findOne(e);

        String itemName = "이케아침대";
        Bed savedItem = bedRepository.save(Bed.builder()
                .categoryCode(one.orElseThrow(() -> new Exception("가구_침대_침대프레임_일반침대 항목 없음")))
                .name(itemName)
                .build());
        //when
        List<Bed> all = bedRepository.findByName(itemName);
        Bed item = all.get(0);
        item.update(null,"한샘침대",null,null,null,null);
        bedRepository.save(item);

        Optional<Bed> itemForCompare = bedRepository.findById(savedItem.getId());

        //then
        Assertions.assertThat(itemForCompare.orElseThrow(()->new Exception("Fail to find 등록된 한샘침대")).getName()).isEqualTo(item.getName());
    }
}
