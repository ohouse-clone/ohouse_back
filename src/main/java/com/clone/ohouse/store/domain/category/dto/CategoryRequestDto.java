package com.clone.ohouse.store.domain.category.dto;

import com.clone.ohouse.store.domain.category.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
        value = "Category API Request",
        description = "카테고리를 등록, 수정, 삭제, 찾을때 DTO 로 사용됩니다. <br>" +
                "수정의 경우에는 수정할 부분만 사용하세요, 전체 프로퍼티를 결정할 필요는 없습니다."
)
@NoArgsConstructor
@Getter
public class CategoryRequestDto {

    @ApiModelProperty(
            value = "카테고리 id"
    )
    private Long id;
    @ApiModelProperty(
            value = "카테고리 설명",
            required = true)
    private String name;

    @ApiModelProperty(
            value = "카테고리 코드",
            required = true
    )
    private Long code;

    @ApiModelProperty(
            value = "부모 카테고리가 있는 경우에 할당됩니다. 등록의 경우엔 부모카테고리 id를 입력해야합니다"
    )
    private Long parentId;

    @ApiModelProperty(
            value = "카테고리의 자식 카테고리들입니다. 하지만 자식들의 카테고리의 자식까지 모두 가져오지않습니다."
    )
    private List<CategoryRequestDto> child = new ArrayList<>();

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
        this.id = entity.getId();
        this.name = entity.getName();
        this.code = entity.getCode();
    }

    public void setChild(List<CategoryRequestDto> child) {
        this.child = child;
    }
    public void setParentId(Long id){
        this.parentId = id;
    }

    public Category toEntity(){
        return new Category(name, code);
    }
}
