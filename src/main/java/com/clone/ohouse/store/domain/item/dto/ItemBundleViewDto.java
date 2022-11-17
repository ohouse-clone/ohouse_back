package com.clone.ohouse.store.domain.item.dto;

import com.clone.ohouse.store.domain.item.ItemRequestDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemBundleViewDto {
    private Long totalNum;
    private List<? extends ItemRequestDto> items;

    public ItemBundleViewDto(Long totalNum, List<? extends ItemRequestDto> items) {
        this.totalNum = totalNum;
        this.items = items;
    }
}
