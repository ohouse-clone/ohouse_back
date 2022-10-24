package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.Category;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategory;

import java.util.List;

public interface ItemCategoryCustom {
    List<ItemCategory> findItemCategoryListForItem(Long itemId);
}
