package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategoryRepositoryImpl;
import com.clone.ohouse.store.domain.category.dto.CategoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/api/v1/category")
    public Long save(@RequestBody CategoryRequestDto saveRequestDto){
        Category category = new Category(saveRequestDto.getName(), saveRequestDto.getCode());
        if(saveRequestDto.getParentId() != null)
            category.addParent(categoryRepository.findById(saveRequestDto.getParentId()).orElse(null));
        return categoryRepository.save(category).getId();
    }
    @PutMapping("/store/api/v1/category/{id}")
    public HttpEntity<CategoryRequestDto> update(@PathVariable Long id, @RequestBody CategoryRequestDto updateRequestDto){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Category parent = categoryRepository.findById(updateRequestDto.getParentId()).orElse(null);

        category.update(updateRequestDto.getName(), updateRequestDto.getCode(), parent);
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/store/api/v1/category/{id}")
    public HttpEntity<CategoryRequestDto> findById(@PathVariable Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new CategoryRequestDto(category), HttpStatus.OK);
    }


    @DeleteMapping("/store/api/v1/category/{id}")
    public void delete(@PathVariable Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) categoryRepository.delete(category);
    }
}
