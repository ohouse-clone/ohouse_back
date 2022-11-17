package com.clone.ohouse.store.domain.item;

import com.clone.ohouse.store.domain.category.CategorySearch;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> findByCategory(CategorySearch categorySearch);
}
