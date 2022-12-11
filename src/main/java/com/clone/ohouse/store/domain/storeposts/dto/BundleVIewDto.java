package com.clone.ohouse.store.domain.storeposts.dto;

import com.clone.ohouse.store.domain.storeposts.dto.StorePostsViewDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@ApiModel(
        description = "StorePost Query API(GET /store/category)에 대한 응답"
)
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
