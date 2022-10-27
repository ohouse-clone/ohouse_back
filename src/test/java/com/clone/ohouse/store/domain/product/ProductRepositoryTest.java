//package com.clone.ohouse.store.domain.product;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class ProductRepositoryTest {
//    @Autowired
//    private BedRepository bedRepository;
//    @Autowired
//    private ItemCategoryCodeRepository itemCategoryCodeRepository;
//    @Autowired
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    public void updatePreviously() {
//        //Find Category 0-22-20-20 : 가구_침대_침대프레임_일반침대
//        ItemCategoryCode savedCode = itemCategoryCodeRepository.findByCategory1AndCategory2AndCategory3AndCategory4(0,22,20,20);
//
//        //Set Item
//        String itemName1 = "이케아침대";
//        Bed savedItem1 = bedRepository.save(Bed.builder()
//                .categoryCode(savedCode)
//                .name(itemName1)
//                .color("red")
//                .build());
//        //Set Item2
//        String itemName2 = "한샘침대";
//        Bed savedItem2 = bedRepository.save(Bed.builder()
//                .categoryCode(savedCode)
//                .name(itemName2)
//                .color("blue")
//                .build());
//        //Set Item3
//        String itemName3 = "시몬스침대";
//        Bed savedItem3 = bedRepository.save(Bed.builder()
//                .categoryCode(savedCode)
//                .name(itemName3)
//                .color("white")
//                .build());
//
//        bedRepository.save(savedItem1);
//        bedRepository.save(savedItem2);
//        bedRepository.save(savedItem3);
//    }
//
//    @AfterEach
//    public void cleanUp() {
//        productRepository.deleteAll();
//        bedRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("제품 등록")
//    public void 제품등록() {
//        //given
//        String itemName1 = "이케아침대";
//        String itemName2 = "한샘침대";
//        String itemName3 = "시몬스침대";
//
//        List<Bed> itemList1 = bedRepository.findByName(itemName1);
//        List<Bed> itemList2 = bedRepository.findByName(itemName2);
//        List<Bed> itemList3 = bedRepository.findByName(itemName3);
//
//        List<String> productNames = new ArrayList<>();
//        productNames.add("멜로우 하단 메트리스 (SS/Q)");
//        productNames.add("알렌 F 원목 침대");
//        productNames.add("피아프 LED 침대");
//
//        Product product1 = Product.builder()
//                .item(itemList1.get(0))
//                .productName(productNames.get(0))
//                .price(440000)
//                .stock(100)
//                .rateDiscount(44)
//                .build();
//        Product product2 = Product.builder()
//                .item(itemList2.get(0))
//                .productName(productNames.get(1))
//                .price(600000)
//                .stock(50)
//                .rateDiscount(60)
//                .build();
//        Product product3 = Product.builder()
//                .item(itemList3.get(0))
//                .productName(productNames.get(2))
//                .price(300000)
//                .stock(100)
//                .rateDiscount(20)
//                .build();
//        //when
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//
//        //then
//        List<Product> all = productRepository.findAll();
//
//        for(String productName : productNames){
//            boolean is_exist = false;
//            for(Product p : all) {
//                if(p.getProductName().equals(productName)) {
//                    is_exist = true;
//                    break;
//                }
//            }
//            Assertions.assertThat(is_exist).isTrue();
//        }
//    }
//
//    @Test
//    @DisplayName("등록된 제품 제거")
//    public void 등록된제품제거() {
//        //given
//        String itemName1 = "이케아침대";
//        List<Bed> itemList1 = bedRepository.findByName(itemName1);
//
//        String productName1 = "알렌 F 원목 침대";
//        Product product1 = Product.builder()
//                .item(itemList1.get(0))
//                .productName(productName1)
//                .price(440000)
//                .stock(100)
//                .rateDiscount(44)
//                .build();
//        Product saved = productRepository.save(product1);
//
//        //when
//        List<Product> all = productRepository.findAll();
//        Long productId = all.get(0).getId();
//
//        productRepository.delete(saved);
//
//        //then
//        Optional<Product> findId = productRepository.findById(productId);
//        Assertions.assertThat(findId.isEmpty()).isTrue();
//    }
//
//    @Test
//    @DisplayName("등록된 제품 수정")
//    public void 등록된제품수정() throws Exception{
//        //given
//        String itemName1 = "이케아침대";
//        List<Bed> itemList1 = bedRepository.findByName(itemName1);
//
//        String productName1 = "알렌 F 원목 침대";
//        Product product1 = Product.builder()
//                .item(itemList1.get(0))
//                .productName(productName1)
//                .price(440000)
//                .stock(100)
//                .rateDiscount(44)
//                .build();
//        Product saved = productRepository.save(product1);
//
//        //when
//        String changedName = "빨간침대";
//        saved.update(
//                saved.getItem(),
//                changedName,
//                saved.getStock(),
//                saved.getPrice(),
//                saved.getRateDiscount());
//        productRepository.save(saved);
//
//        //then
//        Optional<Product> findId = productRepository.findById(saved.getId());
//        Assertions.assertThat(findId.orElseThrow(() -> new Exception("Fail to find")).getProductName()).isEqualTo(changedName);
//    }
//}
