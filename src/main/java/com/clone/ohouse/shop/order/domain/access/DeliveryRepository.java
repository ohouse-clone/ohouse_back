package com.clone.ohouse.shop.order.domain.access;

import com.clone.ohouse.shop.order.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
