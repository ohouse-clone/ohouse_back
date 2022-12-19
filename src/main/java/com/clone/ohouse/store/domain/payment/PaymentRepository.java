package com.clone.ohouse.store.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderApprovalCode(String orderApprovalCode);
}
