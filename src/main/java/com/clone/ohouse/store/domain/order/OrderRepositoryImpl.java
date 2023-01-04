package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.store.domain.payment.QPayment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.clone.ohouse.store.domain.order.QOrder.order;
import static com.clone.ohouse.store.domain.order.QOrderedProduct.orderedProduct;
import static com.clone.ohouse.store.domain.payment.QPayment.payment;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findByIdWithOrderedProduct(Long id) {
        Order findOrder = queryFactory
                .select(order)
                .from(order)
                .leftJoin(order.orderedProducts, orderedProduct).fetchJoin()
                .where(order.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> findByIdWithStorePostsAndOrderedProduct(Long id) {
        //TODO: 주문상세조회 쿼리 구현
//        queryFactory
//                .select(order)
//                .from(order)
//                .leftJoin(order.orderedProducts, orderedProduct).fetchJoin()
//                .leftJoin(order.payment, payment).fetchJoin()
//                .leftJoin(order.st)

        return Optional.empty();
    }

    @Override
    public Optional<Order> findByOrderIdWithOrderedProduct(String orderId) {
        Order findOrder = queryFactory
                .select(order)
                .from(order)
                .leftJoin(order.orderedProducts, orderedProduct).fetchJoin()
                .leftJoin(order.payment, payment).fetchJoin()
                .where(payment.orderId.eq(orderId))
                .fetchOne();
        return Optional.ofNullable(findOrder);
    }




}
