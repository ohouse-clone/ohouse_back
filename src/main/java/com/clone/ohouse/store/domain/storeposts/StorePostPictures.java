package com.clone.ohouse.store.domain.storeposts;

import com.clone.ohouse.utility.s3.S3File;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class StorePostPictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 512)
    private String s3Url;
    @Column(length = 128)
    private String keyName;
    @Column
    private String localFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_posts_id")
    private StorePosts storePosts;

    @Builder
    public StorePostPictures(String s3Url, String keyName, String localFilePath) {
        this.s3Url = s3Url;
        this.keyName = keyName;
        this.localFilePath = localFilePath;
    }

    public void registerStorePost(StorePosts storePost){
        this.storePosts = storePost;
        storePost.getStorePostPictures().add(this);
    }

    public void registerKey(S3File file){
        this.keyName = file.getKey();
        this.s3Url = file.getUrl();
    }
}
