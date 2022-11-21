package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryParser;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
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
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ItemApiController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/item/bed")
    HttpEntity<Long> save(@RequestParam String category, @RequestBody BedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/item/storagebed")
    HttpEntity<Long> save(@RequestParam String category, @RequestBody StorageBedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/store/item/{id}")
    void delete(@PathVariable Long id) throws Exception{
        itemService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/store/items")
    HttpEntity<ItemBundleViewDto> findByCategory(@RequestParam String category, Pageable pageable) throws Exception{
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);

        if(categorySearch.getCode1() == null || categorySearch.getCode2() == null || categorySearch.getCode3() == null || categorySearch.getCode4() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(itemService.findByCategory(categorySearch, pageable), HttpStatus.OK);
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
