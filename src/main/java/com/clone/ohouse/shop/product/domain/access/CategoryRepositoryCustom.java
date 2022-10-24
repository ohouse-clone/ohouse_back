package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.dto.CategoryIdsDto;
import com.clone.ohouse.shop.product.domain.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
     Category findCategory(CategorySearch condition);

     CategoryIdsDto findCategoryIds(CategorySearch condition);
}
