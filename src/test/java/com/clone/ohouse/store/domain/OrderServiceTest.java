package com.clone.ohouse.store.domain;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.store.domain.order.Order;
import com.clone.ohouse.store.domain.order.OrderRepository;
import com.clone.ohouse.store.domain.order.OrderedProduct;
import com.clone.ohouse.store.domain.order.OrderedProductRepository;
import com.clone.ohouse.store.domain.order.dto.DeliveryDto;
import com.clone.ohouse.store.domain.order.dto.OrderRequestDto;
import com.clone.ohouse.store.domain.order.dto.OrderResponse;
import com.clone.ohouse.store.domain.order.dto.OrderedProductDto;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.payment.PaymentRepository;
import com.clone.ohouse.store.domain.payment.PaymentType;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceTest {
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
    void OrderStart() throws Exception {
        //given
        OrderedProductDto orderedProductDto3 = new OrderedProductDto(savedProduct3.getId(), savedProduct3.getPrice(), 10L);
        OrderedProductDto orderedProductDto4 = new OrderedProductDto(savedProduct4.getId(), savedProduct4.getPrice(), 20L);
        OrderedProductDto orderedProductDto5 = new OrderedProductDto(savedProduct5.getId(), savedProduct5.getPrice(), 30L);
        OrderedProductDto orderedProductDto6 = new OrderedProductDto(savedProduct6.getId(), savedProduct6.getPrice(), 40L);
        ArrayList<OrderedProductDto> orderedProductList = new ArrayList<>(List.of(
                orderedProductDto3,
                orderedProductDto4,
                orderedProductDto5,
                orderedProductDto6));
        OrderRequestDto orderRequestDto = new OrderRequestDto(PaymentType.CARD, "티셔츠 외 1건", orderedProductList);
        DeliveryDto deliveryDto = new DeliveryDto("sender1", "receiver1", "200", "내집", "경기도", "서울시", "010-0000-0000", "빨리와주세요");

        //temporary user
        SessionUser sessionUser = new SessionUser(User.builder().email("tester_1@cloneohouse.shop").build());

        //when
        OrderResponse orderResponse = orderService.orderStart(sessionUser, orderRequestDto, deliveryDto);

        //then
        System.out.println("orderId for toss : " + orderResponse.getOrderId());
        Assertions.assertThat(orderResponse.getName()).isEqualTo("티셔츠 외 1건");
        Assertions.assertThat(orderResponse.getTotalPrice()).isEqualTo(5800L); //1300+1400+1500+1600 = 5800
        Assertions.assertThat(orderResponse.getSuccessUrl()).isEqualTo(paymentService.getTossSuccessCallBackUrlForCard());
        Assertions.assertThat(orderResponse.getFailUrl()).isEqualTo(paymentService.getTossFailCallBackUrlForCard());

        Payment payment = paymentRepository.findByOrderId(orderResponse.getOrderId()).orElseThrow(() -> new NoSuchElementException("해당 orderId를 가진 payment가 없음 : " + orderResponse.getOrderId()));
        Order order = orderRepository.findByPayment(payment).orElseThrow(() -> new NoSuchElementException("해당 payment와 관게를 맺은 order가 없음 "));
        List<OrderedProduct> orderedProducts = order.getOrderedProducts();

        Assertions.assertThat(orderedProducts.size()).isEqualTo(4);
        Assertions.assertThat(orderedProducts.get(0).getAmount()).isEqualTo(10L);
        Assertions.assertThat(orderedProducts.get(1).getAmount()).isEqualTo(20L);
        Assertions.assertThat(orderedProducts.get(2).getAmount()).isEqualTo(30L);
        Assertions.assertThat(orderedProducts.get(3).getAmount()).isEqualTo(40L);
        Assertions.assertThat(order.getDelivery().getMemo()).isEqualTo("빨리와주세요");

    }
}
