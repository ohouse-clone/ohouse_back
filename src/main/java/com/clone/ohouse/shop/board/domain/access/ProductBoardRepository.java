package com.clone.ohouse.shop.board.domain.access;

import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBoardRepository extends JpaRepository<ProductBoard, Long> {
}
