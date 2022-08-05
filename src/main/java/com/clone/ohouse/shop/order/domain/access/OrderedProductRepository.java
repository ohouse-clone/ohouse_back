package com.clone.ohouse.shop.order.domain.access;

import com.clone.ohouse.shop.order.domain.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}
