package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.store.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPayment(Payment payment);
}
