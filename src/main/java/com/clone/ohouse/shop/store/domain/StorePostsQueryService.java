package com.clone.ohouse.shop.store.domain;

import com.clone.ohouse.shop.product.domain.access.CategoryRepository;
import com.clone.ohouse.shop.product.domain.access.CategorySearch;
import com.clone.ohouse.shop.product.domain.entity.Category;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.store.domain.access.StorePostsRepository;
import com.clone.ohouse.shop.store.domain.dto.BundleVIewDto;
import com.clone.ohouse.shop.store.domain.dto.StorePostsBundleViewDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class StorePostsQueryService {
    private final CategoryRepository categoryRepository;
    private final StorePostsRepository storePostsRepository;
    @Transactional
    public List<StorePosts> getBundleViewV1(CategorySearch condition, Pageable pageable){
        Category category = categoryRepository.findCategory(condition);

        List<StorePosts> posts = storePostsRepository.getBundleViewByCategoryWithConditionV1(category.getId(), pageable);

        return posts;
    }

    @Transactional
    public List<StorePosts> getBundleViewV2(CategorySearch condition, Pageable pageable){
        Category category = categoryRepository.findCategory(condition);

        List<StorePosts> posts = storePostsRepository.getBundleViewByCategoryWithConditionV2(category.getId(), pageable);

        return posts;
    }

    @Transactional
    public BundleVIewDto getBundleViewV3(CategorySearch condition, Pageable pageable){
        Category category = categoryRepository.findCategory(condition);

        BundleVIewDto result = storePostsRepository.getBundleViewByCategoryWithConditionV3(category.getId(), pageable);

        return result;
    }
}
