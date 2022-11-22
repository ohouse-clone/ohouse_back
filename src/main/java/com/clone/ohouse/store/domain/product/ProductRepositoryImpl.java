package com.clone.ohouse.store.domain.product;

import com.clone.ohouse.store.domain.item.QItem;
import com.clone.ohouse.store.domain.storeposts.QStorePosts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.clone.ohouse.store.domain.item.QItem.*;
import static com.clone.ohouse.store.domain.product.QProduct.*;
import static com.clone.ohouse.store.domain.storeposts.QStorePosts.*;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<Product> findByIdWithFetch(Long id) {
        return Optional.of(queryFactory
                .select(product)
                .from(product)
                .join(product.storePosts, storePosts).fetchJoin()
                .join(product.item, item).fetchJoin()
                .where(product.id.eq(id))
                .fetchOne());
    }
}
