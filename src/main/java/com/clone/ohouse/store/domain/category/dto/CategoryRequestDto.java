package com.clone.ohouse.store.domain.category.dto;

import com.clone.ohouse.store.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryRequestDto {
    private String name;
    private Long code;
    private Long parentId;
    private List<Category> child = new ArrayList<>();

    public CategoryRequestDto(String name, Long code) {
        this.name = name;
        this.code = code;
    }

    public CategoryRequestDto(String name, Long code, Long parentId) {
        this.name = name;
        this.code = code;
        this.parentId = parentId;
    }

    public CategoryRequestDto(Category entity) {
        this.name = entity.getName();
        this.code = entity.getCode();
        this.parentId = entity.getParent().getId();
        this.child = entity.getChild();
    }

    public Category toEntity(){
        return new Category(name, code);
    }
}
