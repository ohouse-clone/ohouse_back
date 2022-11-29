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
    @Column(name = "store_post_picture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String s3Url;
    @Column
    private String key;
    @Column
    private String localPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_post_id")
    private StorePosts storePosts;

    @Builder
    public StorePostPictures(String s3Url, String key, String localPath) {
        this.s3Url = s3Url;
        this.key = key;
        this.localPath = localPath;
    }

    public void registerStorePost(StorePosts storePost){
        this.storePosts = storePost;
        storePost.getStorePostPictures().add(this);
    }

    public void registerKey(S3File file){
        this.key = file.getKey();
        this.s3Url = file.getUrl();
    }
}
