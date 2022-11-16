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
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ItemApiController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/store/item/bed")
    HttpEntity<Long> save(@RequestParam String category, @RequestBody BedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }


    @PostMapping("/store/item/storagebed")
    HttpEntity<Long> save(@RequestParam String category, @RequestBody StorageBedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }

    @DeleteMapping("/store/item/{id}")
    void delete(@PathVariable Long id) throws Exception{
        itemService.delete(id);
    }


    private HttpEntity<Long> saveWithValidate(String category, ItemRequestDto requestDto) throws Exception {
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
