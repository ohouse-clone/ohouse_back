package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.ItemCategoryRepository;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.category.ItemCategory;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import com.clone.ohouse.store.domain.item.bed.dto.BedRequestDto;
import com.clone.ohouse.store.domain.item.bed.dto.StorageBedRequestDto;
import com.clone.ohouse.store.domain.item.dto.ItemBundleViewDto;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void delete(Long id) throws Exception{
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("잘못된 id입니다"));

        detachCategoryTagIn(item);
        itemRepository.delete(item);
    }

    @Transactional
    public ItemBundleViewDto findByCategory(CategorySearch condition, Pageable pageable, ItemSearchCondition itemSearchCondition) throws Exception{
        Category category = categoryRepository.findCategory(condition);
        Class type = new ItemSelector().selectTypeFrom(category.getName()).orElse(null);

        if(category == null) throw new RuntimeException("찾으려는 카테고리가 없습니다");
        if (type == null) throw new RuntimeException("찾으려는 카테고리가 없습니다");
        if(itemSearchCondition == null) throw new RuntimeException("itemSearchCondition is null은 허용되지 않습니다");

        Long totalNum = itemRepository.findTotalNumByCategory(category.getId(), itemSearchCondition);
        List<Item> items = itemRepository.findByCategory(category.getId(), pageable, itemSearchCondition);
        ArrayList<? extends ItemRequestDto> list = null;
        if(type == Bed.class)
            list = items.stream().map((i) -> new BedRequestDto((Bed)i)).collect(Collectors.toCollection(ArrayList<BedRequestDto>::new));
        else if(type == StorageBed.class)
            list = items.stream().map((i) -> new StorageBedRequestDto((StorageBed)i)).collect(Collectors.toCollection(ArrayList<StorageBedRequestDto>::new));
        else throw new RuntimeException("매칭되는 카테고리와 타입이 없습니다");

        return new ItemBundleViewDto(totalNum, Long.valueOf(list.size()) ,list);
    }

    private void attachCategoryTagWith(CategorySearch condition , Item item) throws Exception {
        if(condition.getCode1() == null ) throw new IllegalAccessException();

        List<Category> categories = categoryRepository.findCategories(condition);

        for(int i = 0; i < categories.size(); ++i){
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
