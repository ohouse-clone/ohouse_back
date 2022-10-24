package com.clone.ohouse.shop.product.domain;

import com.clone.ohouse.shop.product.domain.access.CategoryRepository;
import com.clone.ohouse.shop.product.domain.access.CategorySearch;
import com.clone.ohouse.shop.product.domain.access.ItemCategoryRepository;
import com.clone.ohouse.shop.product.domain.access.ItemRepository;
import com.clone.ohouse.shop.product.domain.dto.CategoryIdsDto;
import com.clone.ohouse.shop.product.domain.entity.Category;
import com.clone.ohouse.shop.product.domain.entity.Item;
import com.clone.ohouse.shop.product.domain.entity.ItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemCategoryRepository itemCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    private void attachCategoryTagWith(CategorySearch condition , Item item) throws Exception {
        if(condition.getCode1() == null || condition.getCode2() == null || condition.getCode3() == null || condition.getCode4() == null) throw new IllegalAccessException();

        CategoryIdsDto categoryIds = categoryRepository.findCategoryIds(condition);
        itemCategoryRepository.save(new ItemCategory())



    }


    private void detachCategoryTagIn(Item item) {

    }




}
