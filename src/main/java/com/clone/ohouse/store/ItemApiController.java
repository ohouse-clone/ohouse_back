package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryParser;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.bed.dto.BedRequestDto;
import com.clone.ohouse.store.domain.item.bed.dto.StorageBedRequestDto;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemApiController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/store/item")
    HttpEntity<Long> save(@RequestParam String category, @RequestBody BedRequestDto requestDto) throws Exception{
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        Category categoryEntity = categoryRepository.findCategory(categorySearch);

        if(categoryEntity == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(categorySearch.getCode4() == null ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Class type = new ItemSelector().selectTypeFrom(categoryEntity.getName()).orElse(null);
        Class.

        Long savedId = itemService.save(requestDto.toEntity(), categorySearch);

        return new ResponseEntity<Long>(savedId, HttpStatus.OK);
    }


    @PostMapping("/store/item")
    HttpEntity<Long> save(@RequestParam String category, @RequestBody StorageBedRequestDto requestDto) throws Exception{
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        Category categoryEntity = categoryRepository.findCategory(categorySearch);
        if(categoryEntity == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(categorySearch.getCode4() == null ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long savedId = itemService.save(requestDto.toEntity(), categorySearch);

        return new ResponseEntity<Long>(savedId, HttpStatus.OK);
    }
}
