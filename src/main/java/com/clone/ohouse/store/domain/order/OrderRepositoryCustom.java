package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.store.domain.payment.Payment;

import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<Order> findByIdWithOrderedProduct(Long id);
    Optional<Order> findByOrderIdWithOrderedProduct(String orderId);
}
