package com.clone.ohouse.store.domain.product;

import java.util.Optional;

public interface ProductRepositoryCustom {
    Optional<Product> findByIdWithFetchJoin(Long id);
}
