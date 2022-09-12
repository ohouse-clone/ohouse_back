package com.clone.ohouse.shop.store.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StorePostsBundleViewResponse {
    public int bundleSize = 0;
    public List<StorePostsBundleViewDto> bundle;
}
