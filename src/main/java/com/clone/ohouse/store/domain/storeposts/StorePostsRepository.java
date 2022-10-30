package com.clone.ohouse.store.domain.storeposts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePostsRepository extends JpaRepository<StorePosts, Long> , StorePostsRepositoryCustom{
//    @Query("select distinct s from StorePosts s join fetch s.productList p join fetch p.item i join fetch i.categoryCode c where c.category1 = :category1 and c.category2 = :category2 and c.category3 = :category3 and c.category4 = :category4 order by p.popular desc")
//    List<StorePosts> findBundleViewByCategoryOrderByPopular(@Param("category1") Integer code1, @Param("category2") Integer code2, @Param("category3") Integer code3, @Param("category4") Integer code4, Pageable pageable);
}
