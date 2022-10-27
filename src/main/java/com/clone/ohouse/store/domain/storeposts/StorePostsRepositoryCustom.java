package com.clone.ohouse.store.domain.storeposts;

import com.clone.ohouse.store.domain.storeposts.dto.BundleVIewDto;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface StorePostsRepositoryCustom {
    List<StorePosts> getBundleViewByCategoryWithConditionV1(Long categoryId, Pageable pageable);
    List<StorePosts> getBundleViewByCategoryWithConditionV2(Long categoryId, Pageable pageable);
    BundleVIewDto getBundleViewByCategoryWithConditionV3(Long categoryId, Pageable pageable);
}
