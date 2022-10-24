package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.Category;

public interface CategoryRepositoryCustom {
     Category findCategory(CategorySearch condition);
}
