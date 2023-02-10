package com.clone.ohouse.store.domain.item.dto;

import com.clone.ohouse.store.domain.item.ItemRequestDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
