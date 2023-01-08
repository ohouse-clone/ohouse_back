package com.clone.ohouse.store.domain.product;

import com.clone.ohouse.store.domain.item.QItem;
import com.clone.ohouse.store.domain.storeposts.QStorePosts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.clone.ohouse.store.domain.item.QItem.*;
import static com.clone.ohouse.store.domain.product.QProduct.*;
import static com.clone.ohouse.store.domain.storeposts.QStorePosts.*;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<Product> findByIdWithFetchJoin(Long id) {
        return Optional.ofNullable(queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.storePosts, storePosts).fetchJoin()
                .leftJoin(product.item, item).fetchJoin()
                .where(product.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<Product> findByItemId(Pageable pageable, Long itemId, ProductSearchCondition productSearchCondition) {
        return queryFactory
                .select(product)
                .from(product)
                .join(product.item, item).fetchJoin()
                .where(item.id.eq(itemId),
                        productSearchCondition.eqProductName(),
                        productSearchCondition.goePriceBegin(),
                        productSearchCondition.loePriceEnd(),
                        productSearchCondition.goeStock(),
                        productSearchCondition.goePopular())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countByItemId(Long itemId, ProductSearchCondition productSearchCondition) {
        return queryFactory
                .select(product.count())
                .from(product)
                .join(product.item, item)
                .where(item.id.eq(itemId),
                        productSearchCondition.eqProductName(),
                        productSearchCondition.goePriceBegin(),
                        productSearchCondition.loePriceEnd(),
                        productSearchCondition.goeStock(),
                        productSearchCondition.goePopular())
                .fetchOne();
    }

    @Override
    public void updateBulkWithStorePostId(Long storePostId, List<Long> productIds) {
        queryFactory
                .update(product)
                .set(product.storePosts.id, storePostId)
                .where(product.id.in(productIds))
                .execute();

        em.flush();
        em.clear();
    }

    @Override
    public List<Product> findByIds(List<Long> ids) {
        List<Product> fetch = queryFactory
                .select(product)
                .from(product)
                .where(product.id.in(ids))
                .fetch();

        return fetch;
    }
}
