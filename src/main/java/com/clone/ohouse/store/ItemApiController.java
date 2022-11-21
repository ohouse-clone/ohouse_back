package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryParser;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.dto.BedRequestDto;
import com.clone.ohouse.store.domain.item.bed.dto.StorageBedRequestDto;
import com.clone.ohouse.store.domain.item.dto.ItemBundleViewDto;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ItemApiController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/item/bed")
    public HttpEntity<Long> save(@RequestParam String category, @RequestBody BedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/item/storagebed")
    public HttpEntity<Long> save(@RequestParam String category, @RequestBody StorageBedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/store/item/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        itemService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/store/items")
    public HttpEntity<ItemBundleViewDto> findByCategory(@RequestParam String category, Pageable pageable, @RequestParam(required = false) String itemName, @RequestParam(required = false) String itemModelName, @RequestParam(required = false) String itemBrandName) throws Exception {
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        ItemSearchCondition itemSearchCondition = new ItemSearchCondition();
        itemSearchCondition.itemName = itemName;
        itemSearchCondition.modelName = itemModelName;
        itemSearchCondition.brandName = itemBrandName;

        if (categorySearch.getCode1() == null || categorySearch.getCode2() == null || categorySearch.getCode3() == null || categorySearch.getCode4() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // itemSearchCondition 중 1가지만 null이 아니거나, 모두 null이어야 함
        if (!validateConditionIsOnlyOne(itemSearchCondition)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(itemService.findByCategory(categorySearch, pageable, itemSearchCondition), HttpStatus.OK);
    }
    private boolean validateConditionIsOnlyOne(ItemSearchCondition itemSearchCondition){
        int count = 0;
        if(StringUtils.hasText(itemSearchCondition.itemName)) count++;
        if(StringUtils.hasText(itemSearchCondition.brandName)) count++;
        if(StringUtils.hasText(itemSearchCondition.modelName)) count++;

        if(count > 1) return false;

        return true;
    }

    private HttpEntity<Long> saveWithValidate(String category, ItemRequestDto requestDto) throws Exception {
        //TODO: categoryRepository로 찾는 로직은 Service 안으로 들어가야함, 추후 Fix 예정
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        Category categoryEntity = categoryRepository.findCategory(categorySearch);
        Item item = requestDto.toEntity();
        Class type = new ItemSelector().selectTypeFrom(categoryEntity.getName()).orElse(null);

        if (categoryEntity == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (categorySearch.getCode4() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (type == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (item.getClass() != type) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        Long savedId = itemService.save(item, categorySearch);
        return new ResponseEntity<Long>(savedId, HttpStatus.CREATED);
    }

}
