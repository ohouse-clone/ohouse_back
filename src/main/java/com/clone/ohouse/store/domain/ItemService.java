package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.ItemCategoryRepository;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.category.ItemCategory;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemCategoryRepository itemCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long save(Item item, CategorySearch condition) throws Exception{
        Long id = itemRepository.save(item).getId();
        attachCategoryTagWith(condition, item);

        return id;
    }


    @Transactional
    public void delete(Item item){
        detachCategoryTagIn(item);
        itemRepository.delete(item);
    }

    private void attachCategoryTagWith(CategorySearch condition , Item item) throws Exception {
        if(condition.getCode1() == null || condition.getCode2() == null || condition.getCode3() == null || condition.getCode4() == null) throw new IllegalAccessException();

        List<Category> categories = categoryRepository.findCategories(condition);

        for(int i = 0; i < Category.CATEGORY_SIZE; ++i){
            ItemCategory itemCategory = new ItemCategory(categories.get(i), item);
            itemCategoryRepository.save(itemCategory);
            item.getItemCategories().add(itemCategory);
        }

    }


    private void detachCategoryTagIn(Item item) {
        List<ItemCategory> itemCategories = itemCategoryRepository.findByItem(item);

        for (ItemCategory itemCategory : itemCategories) {
            itemCategoryRepository.delete(itemCategory);
        }
    }
}
