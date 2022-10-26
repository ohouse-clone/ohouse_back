package com.clone.ohouse.shop.store.domain.access;

import com.clone.ohouse.shop.store.domain.dto.BundleVIewDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface StorePostsRepositoryCustom {
    List<StorePosts> getBundleViewByCategoryWithConditionV1(Long categoryId, Pageable pageable);
    List<StorePosts> getBundleViewByCategoryWithConditionV2(Long categoryId, Pageable pageable);
    BundleVIewDto getBundleViewByCategoryWithConditionV3(Long categoryId, Pageable pageable);
}
