package com.clone.ohouse.store.domain.category;

import java.util.List;

public interface CategoryRepositoryCustom {
     Category findCategory(CategorySearch condition);

     List<Category> findCategories(CategorySearch condition);
}
