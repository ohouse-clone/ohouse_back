package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.account.domain.user.QUser;
import com.clone.ohouse.store.domain.payment.QPayment;
import com.clone.ohouse.store.domain.storeposts.QStorePosts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.clone.ohouse.account.domain.user.QUser.*;
import static com.clone.ohouse.store.domain.order.QOrder.order;
import static com.clone.ohouse.store.domain.order.QOrderedProduct.orderedProduct;
import static com.clone.ohouse.store.domain.payment.QPayment.payment;
import static com.clone.ohouse.store.domain.storeposts.QStorePosts.*;

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
    public List<Order> findAllOrders(Long userId){
        List<Order> fetch = queryFactory
                .select(order)
                .from(order)
                .leftJoin(order.orderedProducts, orderedProduct).fetchJoin()
                .leftJoin(order.payment, payment).fetchJoin()
                .leftJoin(order.storePost, storePosts).fetchJoin()
                .leftJoin(order.user, user).fetchJoin()
                .where(user.id.eq(userId))
                .orderBy(order.createTime.asc())
                .fetch();


        return fetch;
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
