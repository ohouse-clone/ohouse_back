package com.clone.ohouse.shop.order.domain;

import com.clone.ohouse.shop.order.domain.access.OrderRepository;
import com.clone.ohouse.shop.order.domain.access.OrderedProductRepository;
import com.clone.ohouse.shop.order.domain.dto.OrderRequestDto;
import com.clone.ohouse.shop.order.domain.dto.OrderedProductDto;
import com.clone.ohouse.shop.order.domain.entity.Delivery;
import com.clone.ohouse.shop.order.domain.entity.Order;
import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.access.BedRepository;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.product.domain.entity.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired private ItemCategoryCodeRepository itemCategoryCodeRepository;
    @Autowired private BedRepository bedRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderedProductRepository orderedProductRepository;
    @Autowired private OrderService service;
    private Product product;
    private Bed item;
    private ItemCategoryCode code;


    @BeforeEach
    void previsoulyUpdate(){
        code = ItemCategoryCode.builder()
                .categoryDetail("가구_침대_침대프레임_일반침대")
                .category1(0)
                .category2(22)
                .category3(20)
                .category4(20)
                .build();

        item = Bed.builder()
                .categoryCode(code)
                .name("시몬스침대")
                .build();

        product = Product.builder()
                .item(item)
                .productName("멜로우 하단 메트리스 (SS/Q)")
                .price(440000)
                .stock(100)
                .rateDiscount(44)
                .build();

        code = itemCategoryCodeRepository.save(code);
        item = bedRepository.save(item);
        product = productRepository.save(product);
    }

    @AfterEach
    void clean(){
        orderedProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        bedRepository.deleteAll();
        itemCategoryCodeRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 저장")
    void save() throws Exception{
        //given
        Long userId = 0L;
        int price1 = 30000;
        int price2 = 60000;
        OrderRequestDto dto = createOrderRquestDto(price1, price2);
        //when
        Long savedOrderId = service.order(userId, dto, new Delivery());

        //then
        Optional<Order> byId = orderRepository.findById(savedOrderId);
        assertThat(byId.get().getTotalPrice()).isEqualTo(price1 + price2);
    }

    @Test
    @DisplayName("주문 취소")
    void delete() throws Exception{
        //given
        Long userId = 0L;
        int price1 = 30000;
        int price2 = 60000;

        OrderRequestDto dto = createOrderRquestDto(price1, price2);
        Long orderedId = service.order(userId, dto, new Delivery());

        //when
        orderRepository.deleteById(orderedId);

        //then
        assertThat(orderRepository.findById(orderedId).isPresent()).isFalse();

    }

    private OrderRequestDto createOrderRquestDto(Integer orderedProductPrice1, Integer orderedProductPrice2){
        OrderedProductDto orderedProductDto1 = new OrderedProductDto();
        orderedProductDto1.setProductId(product.getId());
        orderedProductDto1.setAdjustedPrice(orderedProductPrice1);
        orderedProductDto1.setAmount(10);

        OrderedProductDto orderedProductDto2 = new OrderedProductDto();
        orderedProductDto2.setProductId(product.getId());
        orderedProductDto2.setAdjustedPrice(orderedProductPrice2);
        orderedProductDto2.setAmount(20);


        OrderRequestDto dto = new OrderRequestDto();
        dto.setOrderList(
                new ArrayList<>(Stream.of(
                                orderedProductDto1,
                                orderedProductDto2)
                        .collect(Collectors.toList())));

        return dto;
    }
}
