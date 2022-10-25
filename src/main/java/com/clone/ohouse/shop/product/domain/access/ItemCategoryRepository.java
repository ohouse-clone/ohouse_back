package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> , ItemCategoryCustom{
    List<ItemCategory> findByItem(Item item);
}
