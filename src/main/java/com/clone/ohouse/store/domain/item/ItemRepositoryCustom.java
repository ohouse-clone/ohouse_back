package com.clone.ohouse.store.domain.item;

import com.clone.ohouse.store.domain.category.CategorySearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findByCategory(Long categoryId, Pageable pageable);
    Long findTotalNumByCategory(Long categoryId);
}
