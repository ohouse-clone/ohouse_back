package com.clone.ohouse.shop.order.domain.access;

import com.clone.ohouse.shop.order.domain.entity.Order;
import com.clone.ohouse.shop.order.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findByUser(User user);
}
