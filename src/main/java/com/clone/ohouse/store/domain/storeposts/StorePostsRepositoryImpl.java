package com.clone.ohouse.store.domain.storeposts;

import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsViewDto;
import com.clone.ohouse.store.domain.storeposts.dto.BundleVIewDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAProvider;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;


import java.util.*;

import static com.clone.ohouse.store.domain.category.QItemCategory.itemCategory;
import static com.clone.ohouse.store.domain.item.QItem.item;
import static com.clone.ohouse.store.domain.item.bed.QBed.*;
import static com.clone.ohouse.store.domain.item.bed.QStorageBed.*;
import static com.clone.ohouse.store.domain.product.QProduct.product;
import static com.clone.ohouse.store.domain.storeposts.QStorePosts.storePosts;


@RequiredArgsConstructor
public class StorePostsRepositoryImpl implements StorePostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StorePosts> getBundleViewByCategoryWithConditionV1(Long categoryId, Pageable pageable) {
        return queryFactory
                .select(storePosts)
                .from(storePosts)
                .where(storePosts.id.in(
                        JPAExpressions
                                .select(product.storePosts.id)
                                .from(product)
                                .join(product.item, item)
                                .join(item.itemCategories, itemCategory)
                                .where(itemCategory.category.id.eq(categoryId))
                                .orderBy(product.popular.desc())
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
//        QStorePosts qStorePosts = new QStorePosts("Q");
//        QProduct qProduct = new QProduct("P");
//        return queryFactory
//                .select(storePosts)
//                .from(storePosts)
//                .where(storePosts.id.in(JPAExpressions
//                        .select(qStorePosts.id)
//                        .from(qProduct)
//                        .join(qProduct.storePosts, qStorePosts)
//                        .where(qStorePosts.id.in(1L, 2L, 3L))))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
    }

    @Override
    public List<StorePosts> getBundleViewByCategoryWithConditionV2(Long categoryId, Pageable pageable) {
        return queryFactory
                .select(storePosts).distinct()
                .from(product)
                .join(product.storePosts, storePosts)
                .join(product.item, item)
                .join(item.itemCategories, itemCategory)
                .where(itemCategory.category.id.eq(categoryId).and(product.popular.gt(50)))
//                .where(itemCategory.category.id.eq(categoryId))
                .orderBy(product.popular.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public StorePosts findByIdWithFetchJoinProduct(Long id) {
        return queryFactory
                .select(storePosts)
                .from(storePosts)
                .leftJoin(storePosts.productList, product).fetchJoin()
                .where(storePosts.id.eq(id))
                .fetchOne();
    }

    @Override
    public BundleVIewDto getBundleViewByCategoryWithConditionV3(Long categoryId, Pageable pageable, Optional<Class> type, ItemSearchCondition condition) {
        //Count Query
        JPAQuery<Long> postCountQuery = queryFactory
                .select(storePosts.countDistinct())
                .from(product)
                .join(product.storePosts, storePosts)
                .join(product.item, item)
                .join(item.itemCategories, itemCategory);
        Long count = addQueryByType(postCountQuery, categoryId, condition, type).fetchOne();

        //Data Query
        JPAQuery<Tuple> postQuery = queryFactory
                .select(storePosts,
                        product.popular.sum()).distinct()
                .from(storePosts)
                .join(storePosts.productList, product)
                .join(product.item, item)
                .join(item.itemCategories, itemCategory)
                .groupBy(storePosts.id)
                .orderBy(product.popular.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Tuple> tuples = addQueryByType(postQuery, categoryId, condition, type).fetch();

        //Combination
        List<StorePosts> posts = new ArrayList<>();
        List<Long> postIds = new ArrayList<>();
        Map<Long, Long> popularity = new HashMap<>();
        for (Tuple tuple : tuples) {
            StorePosts post = tuple.get(storePosts);
            Long popularSum = tuple.get(product.popular.sum());
            postIds.add(post.getId());
            posts.add(post);
            popularity.put(post.getId(), popularSum);
        }
        //Combination 2 (Query product)
        Map<Long, Product> productMap = queryFactory
                .select(storePosts.id, product)
                .from(product)
                .join(product.storePosts, storePosts)
                .leftJoin(product.item, item).fetchJoin()
                .where(storePosts.id.in(postIds))
                .orderBy(product.id.asc())
                .transform(GroupBy.groupBy(storePosts.id).as(product));

        List<StorePostsViewDto> views = new ArrayList<>();
        for (StorePosts post : posts) {
            Product product = productMap.get(post.getId());
            Long popularSum = popularity.get(post.getId());
            views.add(new StorePostsViewDto(post, product.getPrice(), product.getRateDiscount(), popularSum, product.getItem().getBrandName()));
        }

        BundleVIewDto result = new BundleVIewDto(count, views.size(), views);
        return result;
    }
    private <T> JPAQuery<T> addQueryByType(JPAQuery<T> prevQuery, Long categoryId ,ItemSearchCondition condition, Optional<Class> type){
        if(type.isEmpty()) return prevQuery.where(eqAll(categoryId, null));
        Class classType = type.get();
        Class conditionType = condition.getClass();
        if(classType == Bed.class && conditionType == BedSearchCondition.class){
            prevQuery.join(bed).on(bed.eq(item))
                            .where(eqAll(categoryId, ((BedSearchCondition) condition).eqBedCondition()));

        }
        else if(classType == StorageBed.class && conditionType == StorageBedCondition.class) {
            prevQuery.join(storageBed).on(storageBed.eq(item))
                    .where(eqAll(categoryId, ((StorageBedCondition) condition).eqStorageBedCondition()));

        }


        return prevQuery;
    }

    private BooleanExpression eqAll(Long categoryId ,BooleanExpression expr){
        return itemCategory.category.id.eq(categoryId).and(expr);
    }

}
