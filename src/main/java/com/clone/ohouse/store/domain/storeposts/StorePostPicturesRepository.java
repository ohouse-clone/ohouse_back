package com.clone.ohouse.store.domain.storeposts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePostPicturesRepository extends JpaRepository<StorePostPictures, Long> {

    List<StorePostPictures> findByStorePostsId(Long storePostId);
}
