package com.clone.ohouse.shop.product.domain.access;


import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemCategoryCodeRepositoryTest {
    @Autowired
    private ItemCategoryCodeRepository itemCategoryCodeRepository;

    private ItemCategoryCode saved;

    @AfterAll
    public void clean(){
        itemCategoryCodeRepository.deleteById(saved.getId());
    }


    @Test
    @Order(1)
    @DisplayName("Save CategoryCode")
    public void SaveItemCategoryCode(){
        ItemCategoryCode code1 = ItemCategoryCode.builder()
                .categoryDetail("TestSave")
                .category1(1000)
                .category2(1001)
                .category3(1002)
                .category4(1003)
                .build();

        saved = itemCategoryCodeRepository.save(code1);

        Optional<ItemCategoryCode> byId = itemCategoryCodeRepository.findById(code1.getId());

        Assertions.assertThat(code1.getCategoryDetail()).isEqualTo(byId.orElseThrow(() -> new NoSuchElementException("해당 내용이 없습니다.")).getCategoryDetail());
    }

    @Test
    @Order(2)
    @DisplayName("Read CategoryCode")
    public void ReadItemCategoryCode() {
        //given
        Integer codeCategory1 = 1000;
        Integer codeCategory2 = 1001;
        Integer codeCategory3 = 1002;
        Integer codeCategory4 = 1003; //가구_침대_침대프레임_일반침대
        //when
        List<ItemCategoryCode> byCategory1 = itemCategoryCodeRepository.findByCategory1OrderByCategory1Desc(codeCategory1);
        List<ItemCategoryCode> list0All = itemCategoryCodeRepository.findByCategory1AndCategory2AndCategory3OrderByCategory1DescCategory2DescCategory3Desc(codeCategory1, codeCategory2, codeCategory3);

        //then
        Assertions.assertThat(byCategory1.get(0).getCategory1()).isEqualTo(codeCategory1);
        Assertions.assertThat(list0All.get(0).getCategory4()).isEqualTo(codeCategory4);
    }

    @Test
    @Order(3)
    @DisplayName("Category1 리스트 얻기")
    public void GetCategory1List(){
        //given

        //when
        List<Integer> category1All = itemCategoryCodeRepository.findDistinctCategory1ListOrderByAsc();

        //then
        Assertions.assertThat(category1All).contains(1000);
    }

    @Test
    @Order(4)
    @DisplayName("Category2 리스트 얻기")
    public void GetCategory2List(){
        //givin

        //when
        List<Integer> category1All = itemCategoryCodeRepository.findDistinctCategory2ListOrderByAsc(1000);

        //then
        Assertions.assertThat(category1All.get(0)).isEqualTo(1001);
    }

    @Test
    @Order(5)
    @DisplayName("Category3 리스트 얻기")
    public void GetCategory3List(){
        //givin

        //when
        List<Integer> category1All = itemCategoryCodeRepository.findDistinctCategory3ListOrderByAsc(1000, 1001);

        //then
        Assertions.assertThat(category1All.get(0)).isEqualTo(1002);
    }

    @Test
    @Order(6)
    @DisplayName("모든 ItemCategoryCode 찾기. Asc으로")
    public void GetItemCategoryServiceListAsc(){
        //given
        List<ItemCategoryCode> allAsc = itemCategoryCodeRepository.findDistinctAllAsc();
        //when

        //then
        /*
        NOTE: 다음 코드는 Assertion이 없어서 무조건 성공으로 끝남

        좋은 방법은 아님을 알고 있지만 정해지지 않은 Table에 대해서 모든 성공 케이스를 검사하는 좋은 방법이 없었음
         */
        for (ItemCategoryCode i : allAsc) {
            System.out.println(i.getCategory1() +" " + i.getCategory2() + " " + i.getCategory3() + " " + i.getCategory4());
        }
    }
}
