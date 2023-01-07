package com.clone.ohouse.store.domain;


import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.store.domain.order.*;
import com.clone.ohouse.store.domain.order.dto.*;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import lombok.RequiredArgsConstructor;
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
    private final StorePostsRepository storePostsRepository;


    public OrderResponse startOrder(SessionUser sessionUser, OrderRequestDto orderRequestDto, DeliveryDto deliveryDto) throws Exception {
        //find user
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new NoSuchElementException("email을 가진 user가 없음 : " + sessionUser.getEmail()));
        //find post
        StorePosts storePost = storePostsRepository.findById(orderRequestDto.getStorePostId()).orElseThrow(() -> new NoSuchElementException("post id가 잘못됨 ; " + orderRequestDto.getStorePostId()));

        //payment create & save
        Payment payment = Payment.createPayment(sessionUser.getName());
        paymentService.save(payment);

        //Delivery create & save
        Delivery delivery = deliveryDto.toEntity();
        deliveryRepository.save(delivery);

        //create order
        Order order = Order.makeOrder(user, delivery, payment, storePost, orderRequestDto.getOrderName());
        Long orderSeq = orderRepository.save(order).getId();

        //create orderedProduct
        //find Products to save in order
        List<OrderedProduct> orderProducts = new ArrayList<>();
        for (var obj : orderRequestDto.getOrderList()) {
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




    public void cancel(String orderId) throws Exception {
        Order findOrder = orderRepository.findByOrderIdWithOrderedProduct(orderId).orElseThrow(() -> new NoSuchElementException("잘못된 주문 번호입니다."));

        findOrder.cancel();
    }

    public OrderBundleViewDto findAllOrders(SessionUser sessionUser) throws Exception {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new NoSuchElementException("email을 가진 user가 없음 : " + sessionUser.getEmail()));
        List<Order> allOrders = orderRepository.findAllOrders(user.getId());

        List<OrderViewDto> orderViewDtos = new ArrayList<>();
        allOrders.forEach((order) -> {
            orderViewDtos.add(new OrderViewDto(
                    order.getPayment().getOrderId(),
                    order.getFixedTime(),
                    order.getStatus(),
                    order.getStorePost().getPreviewImageUrl(),
                    order.getStorePost().getTitle(),
                    order.getTotalPrice()));
        });

        return new OrderBundleViewDto(Long.valueOf(allOrders.size()), orderViewDtos);
    }

    /**
     * 주문 상세 조회
     * 주문정보, 배송정보, 결제정보가 담깁니다.
     * @param sessionUser 로그인한 유저의 유저정보입니다.
     * @param orderId 주문 ID입니다.
     * @return OrderDetailResponseDto
     * @throws Exception
     */
    public OrderDetailResponseDto findOrderDetail(SessionUser sessionUser, String orderId) throws Exception{
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new NoSuchElementException("email을 가진 user가 없음 : " + sessionUser.getEmail()));
        Order order = orderRepository.findOrderDetail(user.getId(), orderId).orElseThrow(() -> new NoSuchElementException("orderId에 해당하는 주문이 없음 : " + orderId));

        return new OrderDetailResponseDto(
                order.getPayment().getOrderId(),
                order.getCreateTime().toString(),
                order.getTotalPrice(),
                new OrderDetailStorePostDto(
                        order.getStorePost().getPreviewImageUrl(),
                        order.getStorePost().getTitle()),
                new OrderDetailDeliveryDto(
                        order.getDelivery().getRecipientName(),
                        order.getDelivery().getPhone(),
                        order.getDelivery().getZipCode(),
                        order.getDelivery().getAddress1(),
                        order.getDelivery().getAddress2(),
                        order.getDelivery().getMemo()
                ));
    }


}
