package com.clone.ohouse.store.domain.storeposts.dto;

import com.clone.ohouse.store.domain.storeposts.dto.StorePostsViewDto;
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
