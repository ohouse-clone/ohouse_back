package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.category.*;
import com.clone.ohouse.store.domain.category.dto.CategoryRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(
        value = "Category API"
)
@RequestMapping("/store/api/v1/category")
@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @ApiOperation(
            value = "카테고리 등록",
            notes = "카테고리를 등록합니다.<br>"+
                    "Response : 저장된 category의 id"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long save(@RequestBody CategoryRequestDto saveRequestDto) {
        Category category = new Category(saveRequestDto.getName(), saveRequestDto.getCode());
        if (saveRequestDto.getParentId() != null)
            category.addParent(categoryRepository.findById(saveRequestDto.getParentId()).orElse(null));
        return categoryRepository.save(category).getId();
    }

    @ApiOperation(
            value = "등록된 카테고리 수정",
            notes = "등록된 카테고리를 수정합니다.<br>" +
                    "Response : Nothing"
    )
    @ApiImplicitParam(name = "id", value = "수정할 카테고리의 id")
    @PutMapping("/{id}")
    public HttpEntity<CategoryRequestDto> update(
            @ApiParam(value = "category id") @PathVariable Long id,
            @RequestBody CategoryRequestDto updateRequestDto) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Category parent = categoryRepository.findById(updateRequestDto.getParentId()).orElse(null);

        category.update(updateRequestDto.getName(), updateRequestDto.getCode(), parent);
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "카테고리 찾기",
            notes = "ID를 아는 카테고리를 찾고 그 자식 카테고리들을 찾습니다 <br>" +
                    "Response : CategoryRequestDto"
    )
    @ApiImplicitParam(name = "id", value = "찾을 카테고리의 id")
    @GetMapping("/{id}")
    public HttpEntity<CategoryRequestDto> findById(
            @ApiParam(value = "category id") @PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CategoryRequestDto result = new CategoryRequestDto(category);
        ArrayList<CategoryRequestDto> child = category.getChild().stream().map((o) -> {
            CategoryRequestDto dto = new CategoryRequestDto(o);
            dto.setParentId(category.getId());
            return dto;
        }).collect(Collectors.toCollection(ArrayList<CategoryRequestDto>::new));


        if(category.getParent() != null) result.setParentId(category.getParent().getId());
        result.setChild(child);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(
            value = "카테고리 삭제",
            notes = "카테고리를 삭제합니다 <br>" +
                    "Response : Nothing"
    )
    @ApiImplicitParam(name = "id", value = "삭제할 카테고리 id")
    @DeleteMapping("/{id}")
    public void delete(
            @ApiParam(value = "category id") @PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) categoryRepository.delete(category);
    }
}
