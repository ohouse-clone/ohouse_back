package com.clone.ohouse.store.order.domain.access;

import com.clone.ohouse.store.domain.order.OrderRepository;
import com.clone.ohouse.store.domain.order.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
        Optional<Order> byId = orderRepository.findById(savedOrder.getId());

        Assertions.assertThat(byId.get().getId()).isEqualTo(savedOrder.getId());

        savedOrderId = byId.get().getId();
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
