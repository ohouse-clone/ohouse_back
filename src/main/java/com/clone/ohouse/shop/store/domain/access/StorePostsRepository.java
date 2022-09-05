package com.clone.ohouse.shop.store.domain.access;

import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePostsRepository extends JpaRepository<StorePosts, Long> {
}
