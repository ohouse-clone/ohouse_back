package com.clone.ohouse.store.domain.item.dto;

import com.clone.ohouse.store.domain.item.ItemRequestDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
        description = "Item API(GET /store/api/v1/item/items) Response<br>" +
                "category와 item 조건를 통해 얻어진 Items들의 모음입니다. <br>" +
                "items 필드는 조회하는 Item 조건에 따라서 동적으로 변경될 수 있습니다."
)
@Getter
public class ItemBundleViewDto {
    private Long totalNum;

    private Long itemNum;
    private List<? extends ItemRequestDto> items;

    public ItemBundleViewDto(Long totalNum, Long itemNum, List<? extends ItemRequestDto> items) {
        this.totalNum = totalNum;
        this.items = items;
        this.itemNum = itemNum;
    }
}
