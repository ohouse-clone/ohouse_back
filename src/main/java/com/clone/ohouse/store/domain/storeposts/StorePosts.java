package com.clone.ohouse.store.domain.storeposts;

import com.clone.ohouse.store.utility.BaseTimeEntity;
import com.clone.ohouse.store.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class StorePosts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isActive = false;

    @Column(length = 200)
    private String title = "제목 없음";

    @Column(length = 100)
    private String previewImageUrl;
    @Column(length = 100)
    private String contentUrl;

    @Column(length = 45, nullable = false)
    private String author;

    @Column(length = 45)
    private String modifiedUser;

    private boolean isDeleted = false;
    private Integer hit = 0;

    @Column(name = "store_post_pictures")
    @OneToMany(mappedBy = "storePosts", fetch = FetchType.LAZY)
    private List<StorePostPictures> storePostPictures = new ArrayList<>();

    @Column(name = "product_list")
    @OneToMany(mappedBy = "storePosts", fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();

    @Builder
    public StorePosts(String title, String contentUrl, String author, String modifiedUser, String previewImageUrl) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.author = author;
        this.modifiedUser = modifiedUser;
        this.previewImageUrl = previewImageUrl;
    }


    public void update(boolean isActive, String title, String contentUrl, String modifiedUser, boolean isDeleted, String previewImageUrl) {
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        if (title != null) this.title = title;
        if (modifiedUser != null) this.modifiedUser = modifiedUser;
        if (contentUrl != null) this.contentUrl = contentUrl;
        if (previewImageUrl != null) this.previewImageUrl = previewImageUrl;
    }

}
