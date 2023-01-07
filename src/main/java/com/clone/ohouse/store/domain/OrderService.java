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
import com.clone.ohouse.store.error.order.OrderError;
import com.clone.ohouse.store.error.order.OrderFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new OrderFailException("찾으려는 user 없음, email : " + sessionUser.getEmail(), OrderError.WRONG_USER_ID));
        //find post
        StorePosts storePost = storePostsRepository.findById(orderRequestDto.getStorePostId()).orElseThrow(() -> new OrderFailException("storePost id가 잘못됨 ; " + orderRequestDto.getStorePostId(), OrderError.WRONG_STORE_POST_ID));

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
        List<Product> products = productRepository.findByIds(orderRequestDto.getOrderList().stream().map((obj) -> obj.getProductId()).collect(Collectors.toCollection(ArrayList<Long>::new)));
        if (products.size() != orderRequestDto.getOrderList().size())
            throw new OrderFailException("orderRequest Product ids 가 DB와 불일치", OrderError.WRONG_PRODUCT_ID);

        List<OrderedProduct> orderProducts = new ArrayList<>();

        orderRequestDto.getOrderList().sort((a, b) -> (int) (a.getProductId() - b.getProductId()));
        products.sort((a, b) -> (int) (a.getId() - b.getId()));

        for (int i = 0; i < products.size(); ++i) {
            Product product = products.get(i);
            OrderedProductDto orderedProductDto = orderRequestDto.getOrderList().get(i);

            OrderedProduct orderedProduct = product.makeOrderedProduct(order, orderedProductDto.getAdjustedPrice(), orderedProductDto.getAmount());

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
        Order findOrder = orderRepository.findByOrderIdWithOrderedProduct(orderId).orElseThrow(() -> new OrderFailException("찾으려는 order 없음, orderId : " + orderId, OrderError.WRONG_ORDER_ID));

        findOrder.cancel();
    }

    public OrderBundleViewDto findAllOrders(SessionUser sessionUser) throws Exception {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new OrderFailException("찾으려는 user 없음, email : " + sessionUser.getEmail(), OrderError.WRONG_USER_ID));
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
     *
     * @param sessionUser 로그인한 유저의 유저정보입니다.
     * @param orderId     주문 ID입니다.
     * @return OrderDetailResponseDto
     * @throws Exception
     */
    public OrderDetailResponseDto findOrderDetail(SessionUser sessionUser, String orderId) throws Exception {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new OrderFailException("찾으려는 user 없음, email : " + sessionUser.getEmail(), OrderError.WRONG_USER_ID));
        Order order = orderRepository.findOrderDetail(user.getId(), orderId).orElseThrow(() -> new OrderFailException("찾으려는 order 없음, orderId : " + orderId, OrderError.WRONG_ORDER_ID));

        return new OrderDetailResponseDto(
                order.getPayment().getOrderId(),
                order.getCreateTime().toString(),
                order.getTotalPrice(),
                order.getStatus(),
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

    public void changeOrderState(SessionUser sessionUser, String orderId, OrderStatus status) throws Exception {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new OrderFailException("찾으려는 user 없음, email : " + sessionUser.getEmail(), OrderError.WRONG_USER_ID));
        Order order = orderRepository.findOrderDetail(user.getId(), orderId).orElseThrow(() -> new OrderFailException("찾으려는 order 없음, orderId : " + orderId, OrderError.WRONG_ORDER_ID));

        order.changeOrderStatus(status);
    }

}
