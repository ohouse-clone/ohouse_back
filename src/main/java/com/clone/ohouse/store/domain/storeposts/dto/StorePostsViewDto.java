package com.clone.ohouse.store.domain.storeposts.dto;


import com.clone.ohouse.store.domain.storeposts.StorePosts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(
        description =
                "Store API (GET /store/category) 요청의 세부 Field <br>" +
                        "BundleViewDto의 Field입니다. <br>" +
                        "StorePost의 Preview입니다."
)
@Getter
public class StorePostsViewDto {
    private Long id;
    private String title;
    private String previewImageUrl;
    private Long popular;
    private Integer price;
    private Integer discountRate;

    private String brandName;

    public StorePostsViewDto(StorePosts postEntity, Integer price, Integer discountRate, Long popular, String brandName) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.previewImageUrl = postEntity.getPreviewImageUrl();
        this.popular = popular;
        this.price = price;
        this.discountRate = discountRate;
        this.brandName = brandName;
    }
}
