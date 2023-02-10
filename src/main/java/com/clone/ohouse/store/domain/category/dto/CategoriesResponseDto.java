package com.clone.ohouse.store.domain.category.dto;

import com.clone.ohouse.store.domain.category.Category;
import lombok.Getter;

@Getter
public class CategoriesResponseDto {
    private Long id;
    private String name;
    private Long code;

    public CategoriesResponseDto(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.code = entity.getCode();
    }
}
