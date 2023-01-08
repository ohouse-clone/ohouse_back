package com.clone.ohouse.store;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.PaymentService;
import com.clone.ohouse.store.domain.order.OrderRepository;
import com.clone.ohouse.store.domain.order.OrderedProductRepository;
import com.clone.ohouse.store.domain.order.dto.*;
import com.clone.ohouse.store.domain.payment.PaymentRepository;
import com.clone.ohouse.store.domain.payment.PaymentType;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderApiControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OrderedProductRepository orderedProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentService paymentService;


    User savedUser = null;
    Product savedProduct1 = null;
    Product savedProduct2 = null;
    Product savedProduct3 = null;
    Product savedProduct4 = null;
    Product savedProduct5 = null;
    Product savedProduct6 = null;
    Product savedProduct7 = null;
    Product savedProduct8 = null;
    Product savedProduct9 = null;

    @Transactional
    @BeforeEach
    void setup() {
        //Setup User
        savedUser = userRepository.save(User.builder()
                .name("TESTER_1")
                .nickname("TSR_1")
                .phone("010-0000-0000")
                .password("1234")
                .email("tester_1@cloneohouse.shop")
                .build());

        //Setup Product
        Product product1 = Product.builder().productName("p1").price(1100L).stock(110L).rateDiscount(10).build();
        Product product2 = Product.builder().productName("p2").price(1200L).stock(120L).rateDiscount(10).build();
        Product product3 = Product.builder().productName("p3").price(1300L).stock(130L).rateDiscount(10).build();
        Product product4 = Product.builder().productName("p4").price(1400L).stock(140L).rateDiscount(10).build();
        Product product5 = Product.builder().productName("p5").price(1500L).stock(150L).rateDiscount(10).build();
        Product product6 = Product.builder().productName("p6").price(1600L).stock(160L).rateDiscount(10).build();
        Product product7 = Product.builder().productName("p7").price(1700L).stock(170L).rateDiscount(10).build();
        Product product8 = Product.builder().productName("p8").price(1800L).stock(180L).rateDiscount(10).build();
        Product product9 = Product.builder().productName("p9").price(1900L).stock(190L).rateDiscount(10).build();
        savedProduct1 = productRepository.save(product1);
        savedProduct2 = productRepository.save(product2);
        savedProduct3 = productRepository.save(product3);
        savedProduct4 = productRepository.save(product4);
        savedProduct5 = productRepository.save(product5);
        savedProduct6 = productRepository.save(product6);
        savedProduct7 = productRepository.save(product7);
        savedProduct8 = productRepository.save(product8);
        savedProduct9 = productRepository.save(product9);
    }
    @AfterEach
    void clean() {
        savedUser = null;
        savedProduct1 = null;
        savedProduct2 = null;
        savedProduct3 = null;
        savedProduct4 = null;
        savedProduct5 = null;
        savedProduct6 = null;
        savedProduct7 = null;
        savedProduct8 = null;
        savedProduct9 = null;
        orderedProductRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
        paymentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void startOrder() throws Exception{
        //given
        String url = "http://localhost:8080" +"/order/api/v1/order";
        OrderedProductDto orderedProductDto3 = new OrderedProductDto(savedProduct3.getId(), savedProduct3.getPrice(), 10L);
        OrderedProductDto orderedProductDto4 = new OrderedProductDto(savedProduct4.getId(), savedProduct4.getPrice(), 20L);
        OrderedProductDto orderedProductDto5 = new OrderedProductDto(savedProduct5.getId(), savedProduct5.getPrice(), 30L);
        OrderedProductDto orderedProductDto6 = new OrderedProductDto(savedProduct6.getId(), savedProduct6.getPrice(), 40L);
        ArrayList<OrderedProductDto> orderedProductList = new ArrayList<>(List.of(
                orderedProductDto3,
                orderedProductDto4,
                orderedProductDto5,
                orderedProductDto6));
        OrderRequestDto orderRequestDto = new OrderRequestDto(PaymentType.CARD, null,"티셔츠 외 1건", orderedProductList);
        DeliveryDto deliveryDto = new DeliveryDto("sender1", "receiver1", "200", "내집", "경기도", "서울시", "010-0000-0000", "빨리와주세요");
        StartOrderRequestDto startOrderRequestDto = new StartOrderRequestDto(orderRequestDto, deliveryDto);

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(startOrderRequestDto))
                )
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        OrderResponse orderResponse = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), OrderResponse.class);
        Assertions.assertThat(orderResponse.getName()).isEqualTo("티셔츠 외 1건");
        Assertions.assertThat(orderResponse.getTotalPrice()).isEqualTo(5800L);//1300+1400+1500+1600 = 5800
        Assertions.assertThat(orderResponse.getSuccessUrl()).isEqualTo(paymentService.getTossSuccessCallBackUrlForCard());
        Assertions.assertThat(orderResponse.getFailUrl()).isEqualTo(paymentService.getTossFailCallBackUrlForCard());
    }

}
