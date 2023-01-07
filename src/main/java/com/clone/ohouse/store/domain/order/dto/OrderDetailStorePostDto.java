package com.clone.ohouse.store.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderDetailStorePostDto {
    private String postPreviewImageUrl;
    private String postTitle;

    public OrderDetailStorePostDto(String postPreviewImageUrl, String postTitle) {
        this.postPreviewImageUrl = postPreviewImageUrl;
        this.postTitle = postTitle;
    }
}
