package com.clone.ohouse.shop.store.domain.access;

import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePostsRepository extends JpaRepository<StorePosts, Long> {
    @Query("select distinct s from StorePosts s join fetch s.productList p join fetch p.item i join fetch i.categoryCode c where c.category1 = :category1 and c.category2 = :category2 and c.category3 = :category3 and c.category4 = :category4 order by p.popular desc")
    List<StorePosts> findBundleViewByCategoryOrderByPopular(@Param("category1") String code1, @Param("category2") String code2, @Param("category3") String code3, @Param("category4") String code4, Pageable pageable);
}
