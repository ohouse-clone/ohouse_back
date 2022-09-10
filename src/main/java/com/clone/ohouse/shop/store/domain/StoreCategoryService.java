package com.clone.ohouse.shop.store.domain;

import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.store.domain.access.StorePostsRepository;
import com.clone.ohouse.shop.store.domain.dto.StorePostsBundleViewDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class StoreCategoryService {

    private final StorePostsRepository storePostsRepository;

    @Transactional
    public List<StorePostsBundleViewDto> findBundleViewAllOrderByPopular(ItemCategoryCode code, Pageable pageable){
        List<StorePosts> bundleView = storePostsRepository.findBundleViewByCategoryOrderByPopular(code.getCategory1(), code.getCategory2(), code.getCategory3(), code.getCategory4(), pageable);

        List<StorePostsBundleViewDto> result = new ArrayList<>();
        for (StorePosts storePosts : bundleView) {
            result.add(new StorePostsBundleViewDto(storePosts));
        }

        return result;
    }
}
