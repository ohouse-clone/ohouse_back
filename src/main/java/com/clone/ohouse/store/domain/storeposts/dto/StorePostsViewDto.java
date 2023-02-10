package com.clone.ohouse.store.domain.storeposts.dto;


import com.clone.ohouse.store.domain.storeposts.StorePosts;
import lombok.Getter;

@Getter
public class StorePostsViewDto {
    private Long id;
    private String title;
    private String previewImageUrl;
    private Long popular;
    private Integer price;
    private Integer discountRate;

    public StorePostsViewDto(StorePosts postEntity, Integer price, Integer discountRate, Long popular) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.previewImageUrl = postEntity.getPreviewImageUrl();
        this.popular = popular;
        this.price = price;
        this.discountRate = discountRate;
    }
}
