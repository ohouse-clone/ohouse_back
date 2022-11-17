package com.clone.ohouse.store.domain.storeposts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePostsRepository extends JpaRepository<StorePosts, Long> , StorePostsRepositoryCustom{

}
