package com.clone.ohouse.store.domain.category;

import com.clone.ohouse.store.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> , ItemCategoryCustom{
    List<ItemCategory> findByItem(Item item);
}
