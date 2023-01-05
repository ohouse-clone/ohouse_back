package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.store.domain.payment.Payment;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<Order> findByIdWithOrderedProduct(Long id);

    List<Order> findAllOrders(Long userId);

    Optional<Order> findByOrderIdWithOrderedProduct(String orderId);
}
