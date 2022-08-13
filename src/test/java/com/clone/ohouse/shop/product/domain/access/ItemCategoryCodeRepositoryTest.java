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


    @AfterAll
    public void clean(){
        itemCategoryCodeRepository.deleteAll();
    }


    @Test
    @Order(1)
    @DisplayName("Save CategoryCode")
    public void SaveItemCategoryCode(){
        ItemCategoryCode code1 = ItemCategoryCode.builder()
                .categoryDetail("가구_침대_침대프레임_일반침대")
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("20")
                .build();
        ItemCategoryCode code2 = ItemCategoryCode.builder()
                .categoryDetail("가구_침대_침대프레임_수납침대")
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("21")
                .build();
        ItemCategoryCode code3 = ItemCategoryCode.builder()
                .categoryDetail("가구_침대_침대+매트리스_일반침대")
                .category1("0")
                .category2("22")
                .category3("21")
                .category4("20")
                .build();
        ItemCategoryCode code4 = ItemCategoryCode.builder()
                .categoryDetail("가구_매트리스.토퍼_매트리스_스프링매트리스")
                .category1("0")
                .category2("23")
                .category3("20")
                .category4("20")
                .build();
        ItemCategoryCode code5 = ItemCategoryCode.builder()
                .categoryDetail("패브릭_여름패브릭_침구_차렵이블")
                .category1("1")
                .category2("20")
                .category3("20")
                .category4("20")
                .build();


        ItemCategoryCode save1 = itemCategoryCodeRepository.save(code1);
        ItemCategoryCode save2 = itemCategoryCodeRepository.save(code2);
        ItemCategoryCode save3 = itemCategoryCodeRepository.save(code3);
        ItemCategoryCode save4 = itemCategoryCodeRepository.save(code4);
        ItemCategoryCode save5 = itemCategoryCodeRepository.save(code5);

        Optional<ItemCategoryCode> byId = itemCategoryCodeRepository.findById(code1.getId());

        Assertions.assertThat(code1.getCategoryDetail()).isEqualTo(byId.orElseThrow(() -> new NoSuchElementException("해당 내용이 없습니다.")).getCategoryDetail());
    }

    @Test
    @Order(2)
    @DisplayName("Read CategoryCode")
    public void ReadItemCategoryCode() {
        //given
        String codeCategory1 = "0";
        String codeCategory2 = "22";
        String codeCategory3 = "20";
        String codeCategory4 = "20"; //가구_침대_침대프레임_일반침대
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
        //In DB, Set 0 is 침대, 1 is 패브릭
        List<String> category1All = itemCategoryCodeRepository.findDistinctCategory1ListOrderByAsc();

        //then
        Assertions.assertThat(category1All.get(0)).isEqualTo("0");
        Assertions.assertThat(category1All.get(1)).isEqualTo("1");
    }

    @Test
    @Order(4)
    @DisplayName("Category2 리스트 얻기")
    public void GetCategory2List(){
        //givin

        //when
        //In DB, Set 0 is 침대, 1 is 패브릭
        //In DB, if category 1 == 0 then category has 22:침대 and 23:매트리스.토퍼 and so on.
        List<String> category1All = itemCategoryCodeRepository.findDistinctCategory2ListOrderByAsc("0");

        //then
        Assertions.assertThat(category1All.get(0)).isEqualTo("22"); //22 : 침대
        Assertions.assertThat(category1All.get(1)).isEqualTo("23"); //23 : 매트리스
    }

    @Test
    @Order(5)
    @DisplayName("Category3 리스트 얻기")
    public void GetCategory3List(){
        //givin

        //when
        //In DB, Set 0 is 침대, 1 is 패브릭
        //In DB, if category 1 == 0 then category has 22:침대 and 23:매트리스.토퍼 and so on.
        //In DB, if category 1 == 0 && category 2 == 22 then category3 is one that start from 20, 20 : 침대프레임, 21 : 침대+매트리스
        List<String> category1All = itemCategoryCodeRepository.findDistinctCategory3ListOrderByAsc("0", "22");

        //then
        Assertions.assertThat(category1All.get(0)).isEqualTo("20"); //20 : 침대프레임
        Assertions.assertThat(category1All.get(1)).isEqualTo("21"); //21 : 침대+매트리스
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
