package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategoryRepositoryImpl;
import com.clone.ohouse.store.domain.category.dto.CategoryRequestDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @ApiOperation(
            value = "카테고리 등록",
            notes = "카테고리를 등록합니다."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/api/v1/category")
    public Long save(@RequestBody CategoryRequestDto saveRequestDto){
        Category category = new Category(saveRequestDto.getName(), saveRequestDto.getCode());
        if(saveRequestDto.getParentId() != null)
            category.addParent(categoryRepository.findById(saveRequestDto.getParentId()).orElse(null));
        return categoryRepository.save(category).getId();
    }

    @ApiOperation(
            value = "등록된 카테고리 수정",
            notes = "등록된 카테고리를 수정합니다."
    )
    @ApiImplicitParam(name = "id", value = "수정할 카테고리의 id")
    @PutMapping("/store/api/v1/category/{id}")
    public HttpEntity<CategoryRequestDto> update(@PathVariable Long id, @RequestBody CategoryRequestDto updateRequestDto){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Category parent = categoryRepository.findById(updateRequestDto.getParentId()).orElse(null);

        category.update(updateRequestDto.getName(), updateRequestDto.getCode(), parent);
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "카테고리 찾기",
            notes = "ID를 아는 카테고리를 찾고 그 자식 카테고리들을 찾습니다"
    )
    @ApiImplicitParam(name = "id", value = "찾을 카테고리의 id")
    @GetMapping("/store/api/v1/category/{id}")
    public HttpEntity<CategoryRequestDto> findById(@PathVariable Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new CategoryRequestDto(category), HttpStatus.OK);
    }


    @ApiOperation(
            value = "카테고리 삭제",
            notes = "카테고리를 삭제합니다"
    )
    @ApiImplicitParam(name = "id", value = "삭제할 카테고리 id")
    @DeleteMapping("/store/api/v1/category/{id}")
    public void delete(@PathVariable Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) categoryRepository.delete(category);
    }
    
    //TODO: CategorySearch 조건에 따른 카테고리 조회 API추가
}
