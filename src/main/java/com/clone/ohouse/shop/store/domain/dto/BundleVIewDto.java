package com.clone.ohouse.shop.store.domain.dto;

import com.clone.ohouse.shop.store.domain.access.StorePostsViewDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class BundleVIewDto {
    private Long totalNum;
    private Integer postsNum;
    private List<StorePostsViewDto> previewPosts;

    public BundleVIewDto(Long totalNum, Integer postsNum, List<StorePostsViewDto> previewPosts) {
        this.totalNum = totalNum;
        this.postsNum = postsNum;
        this.previewPosts = previewPosts;
    }
}
