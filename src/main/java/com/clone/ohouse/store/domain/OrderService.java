package com.clone.ohouse.store.domain;


import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.store.domain.order.*;
import com.clone.ohouse.store.domain.order.dto.DeliveryDto;
import com.clone.ohouse.store.domain.order.dto.OrderRequestDto;
import com.clone.ohouse.store.domain.order.dto.OrderResponse;
import com.clone.ohouse.store.domain.order.dto.OrderedProductDto;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.payment.PaymentRepository;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private final OrderedProductRepository orderedProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public OrderResponse startOrder(SessionUser sessionUser, OrderRequestDto orderRequestDto, DeliveryDto deliveryDto) throws Exception{
        //find user
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(()->new NoSuchElementException("email을 가진 user가 없음 : " + sessionUser.getEmail()));

        //payment create & save
        Payment payment = Payment.createPayment(sessionUser.getName());
        paymentService.save(payment);

        //Delivery create & save
        Delivery delivery = deliveryDto.toEntity();
        deliveryRepository.save(delivery);

        //create order
        Order order = Order.makeOrder(user, delivery, payment, orderRequestDto.getOrderName());
        Long orderSeq = orderRepository.save(order).getId();

        //create orderedProduct
        //find Products to save in order
        List<OrderedProduct> orderProducts = new ArrayList<>();
        for(var obj : orderRequestDto.getOrderList()){
            //TODO: 1개씩 찾고 있는데, in query 등 한방쿼리로 변경필요
            Optional<Product> product = productRepository.findById(obj.getProductId());

            Product findProduct = product.orElseThrow(() -> new RuntimeException("존재하지 않는 상품을 주문합니다."));
            OrderedProduct orderedProduct = findProduct.makeOrderedProduct(order, obj.getAdjustedPrice(), obj.getAmount());
            orderProducts.add(orderedProduct);
            orderedProductRepository.save(orderedProduct);
        }
        order.registerOrderProducts(orderProducts);


        return new OrderResponse(
                order.getTotalPrice(),
                order.getName(),
                payment.getOrderId(),
                order.getCreateTime(),
                paymentService.getTossSuccessCallBackUrlForCard(),
                paymentService.getTossFailCallBackUrlForCard(),
                paymentService.getTossClientApiKeyForTest()
        );
    }

    //TODO: cancel 만들기
    @Transactional
    public void cancel(Long orderSeq) throws Exception {
        Order findOrder = orderRepository.findById(orderSeq).orElseThrow(() -> new NoSuchElementException("잘못된 주문 번호입니다."));

        findOrder.cancel();
        //findOrder.getPayment().cancel();

        List<OrderedProduct> orderedProducts = findOrder.getOrderedProducts();
        for (OrderedProduct orderedProduct : orderedProducts) {
            orderedProductRepository.delete(orderedProduct);
        }


        orderRepository.delete(findOrder);
    }


    @Transactional
    public List<OrderedProduct> findAllOrderedProduct(User user, Long orderSeq){
        return orderRepository.findById(orderSeq)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문번호 입니다."))
                .getOrderedProducts();
    }

}
