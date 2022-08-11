package com.clone.ohouse.shop.order.domain.access;

import com.clone.ohouse.shop.order.domain.dto.OrderedProductDto;
import com.clone.ohouse.shop.order.domain.entity.Delivery;
import com.clone.ohouse.shop.order.domain.entity.Order;
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

import javax.persistence.Transient;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Long savedOrderId;

    @AfterAll
    void clean(){
        orderRepository.deleteAll();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Save Order")
    void save(){
        //given
        //when
        Order savedOrder = orderRepository.save(new Order());

        //then
        Optional<Order> byId = orderRepository.findById(savedOrder.getOrderSeq());

        Assertions.assertThat(byId.get().getOrderSeq()).isEqualTo(savedOrder.getOrderSeq());

        savedOrderId = byId.get().getOrderSeq();
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Delete Order")
    void delete(){
        //given
        //when
        orderRepository.deleteById(savedOrderId);

        //then
        Assertions.assertThat(orderRepository.findById(savedOrderId).isEmpty()).isTrue();
    }

}
