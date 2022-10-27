package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import com.clone.ohouse.store.domain.storeposts.dto.BundleVIewDto;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
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
