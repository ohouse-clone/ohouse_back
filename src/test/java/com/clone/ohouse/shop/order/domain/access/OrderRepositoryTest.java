package com.clone.ohouse.shop.order.domain.access;

import com.clone.ohouse.shop.order.domain.dto.OrderedProductDto;
import com.clone.ohouse.shop.order.domain.entity.Delivery;
import com.clone.ohouse.shop.order.domain.entity.Order;
import com.clone.ohouse.shop.order.domain.entity.OrderedProduct;
import com.clone.ohouse.shop.order.domain.entity.User;
import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.access.ItemRepository;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.product.domain.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private ItemCategoryCodeRepository itemCategoryCodeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    private Product product;
    private Item item;
    private ItemCategoryCode code;


    @BeforeAll
    void previsoulyUpdate(){
        code = ItemCategoryCode.builder()
                .categoryDetail("가구_침대_침대프레임_일반침대")
                .category1("0")
                .category2("22")
                .category3("20")
                .category4("20")
                .build();

        item = Item.builder()
                .categoryCode(code)
                .name("시몬스침대")
                .build();

        product = Product.builder()
                .item(item)
                .productName("멜로우 하단 메트리스 (SS/Q)")
                .price(440000)
                .stock(100)
                .rateDiscount(44)
                .size("SS")
                .color("white")
                .build();

        code = itemCategoryCodeRepository.save(code);
        item = itemRepository.save(item);
        product = productRepository.save(product);
    }

    @AfterAll
    void clean(){
        orderedProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        itemRepository.deleteAll();
        itemCategoryCodeRepository.deleteAll();
    }

    @Test
//    @Order(1)
    @DisplayName("Save Order")
    void save() throws Exception{
        //given
        OrderedProductDto orderedProductDto1 = new OrderedProductDto();
        orderedProductDto1.setProductSeq(product.getProductSeq());
        orderedProductDto1.setAdjustedPrice(30000);
        orderedProductDto1.setAmount(10);

        OrderedProductDto orderedProductDto2 = new OrderedProductDto();
        orderedProductDto2.setProductSeq(product.getProductSeq());
        orderedProductDto2.setAdjustedPrice(60000);
        orderedProductDto2.setAmount(20);

        //when
        Order order = Order.makeOrder(new User(), new Delivery(), new ArrayList<Pair<Product, OrderedProductDto>>(Stream.of(Pair.of(product, orderedProductDto1), Pair.of(product, orderedProductDto2)).collect(Collectors.toList())));
        Order savedOrder = orderRepository.save(order);

        //then
        Optional<Order> byId = orderRepository.findById(savedOrder.getOrderSeq());

        Assertions.assertThat(byId.get().getTotalPrice()).isEqualTo(orderedProductDto1.getAdjustedPrice() + orderedProductDto2.getAdjustedPrice());

    }


}
