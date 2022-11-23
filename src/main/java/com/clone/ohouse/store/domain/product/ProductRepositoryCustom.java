package com.clone.ohouse.store.domain.product;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {
    Optional<Product> findByIdWithFetchJoin(Long id);
    List<Product> findByItemId(Pageable pageable,Long itemId, ProductSearchCondition productSearchCondition);
    Long countByItemId(Long itemId, ProductSearchCondition productSearchCondition);

    void updateBulkWithStorePostId(Long storePostId, List<Long> productIds);
}
