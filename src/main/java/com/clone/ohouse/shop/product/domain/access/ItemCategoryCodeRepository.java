package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategoryCodeRepository extends JpaRepository<ItemCategoryCode, Long> {
    List<ItemCategoryCode> findByCategory1OrderByCategory1Desc(Integer category1);

    List<ItemCategoryCode> findByCategory1AndCategory2OrderByCategory1DescCategory2Desc(Integer category1, Integer category2);

    List<ItemCategoryCode> findByCategory1AndCategory2AndCategory3OrderByCategory1DescCategory2DescCategory3Desc(Integer category1, Integer category2, Integer category3);

    List<ItemCategoryCode> findByCategory1AndCategory2AndCategory3AndCategory4OrderByCategory1DescCategory2DescCategory3DescCategory4Desc(Integer category1, Integer category2, Integer category3, Integer category4);
    ItemCategoryCode findByCategory1AndCategory2AndCategory3AndCategory4(Integer category1, Integer category2, Integer category3, Integer category4);

    @Query("select distinct i.category1 from ItemCategoryCode i order by i.category1 asc")
    List<Integer> findDistinctCategory1ListOrderByAsc();

    @Query("select distinct i.category2 from ItemCategoryCode i where i.category1=:category1 order by i.category2 asc")
    List<Integer> findDistinctCategory2ListOrderByAsc(@Param("category1") Integer category1);

    @Query("select distinct i.category3 from ItemCategoryCode i where i.category1=:category1 and i.category2=:category2 order by i.category3 asc")
    List<Integer> findDistinctCategory3ListOrderByAsc(@Param("category1") Integer category1, @Param("category2") Integer category2);

    @Query("select distinct i from ItemCategoryCode i order by i.category1 asc, i.category2 asc, i.category3 asc, i.category4 asc")
    List<ItemCategoryCode> findDistinctAllAsc();
}
