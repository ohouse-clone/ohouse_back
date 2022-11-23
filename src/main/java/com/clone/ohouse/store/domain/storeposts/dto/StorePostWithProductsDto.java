package com.clone.ohouse.store.domain.storeposts.dto;

import com.clone.ohouse.store.domain.product.dto.ProductDetailDto;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel(
        value = "StorePost에 등록된 product를 함께 조회한 결과"
)
@Getter
public class StorePostWithProductsDto {
    private Long id;
    private boolean isActive;
    private String title;
    private String previewImageUrl;
    private String contentUrl;
    private String author;
    private String modifiedUser;
    private boolean isDeleted;
    private Integer hit;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    private Integer productNum;
    List<ProductDetailDto> products;

    public StorePostWithProductsDto(StorePosts entity, List<ProductDetailDto> products) {
        this.id = entity.getId();
        this.isActive = entity.isActive();
        this.title = entity.getTitle();
        this.previewImageUrl = entity.getPreviewImageUrl();
        this.contentUrl = entity.getContentUrl();
        this.author = entity.getAuthor();
        this.modifiedUser = entity.getModifiedUser();
        this.isDeleted = entity.isDeleted();
        this.hit = entity.getHit();
        this.createDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        if(products != null) this.productNum = products.size();
        this.products = products;
    }
}
