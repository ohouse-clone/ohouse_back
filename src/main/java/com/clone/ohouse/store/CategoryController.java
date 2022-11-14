package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategoryRepositoryImpl;
import com.clone.ohouse.store.domain.category.dto.CategoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @PostMapping("/api/v1/category")
    public void save(@RequestBody CategoryRequestDto saveRequestDto){
        Category category = new Category(saveRequestDto.getName(), saveRequestDto.getCode());
        category.add
        categoryRepository.save()
    }
}
