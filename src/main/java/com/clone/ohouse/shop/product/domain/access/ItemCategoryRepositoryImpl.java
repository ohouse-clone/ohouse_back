package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.clone.ohouse.shop.product.domain.entity.QItem.*;
import static com.clone.ohouse.shop.product.domain.entity.QItemCategory.*;

@RequiredArgsConstructor
public class ItemCategoryRepositoryImpl implements ItemCategoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<ItemCategory> findItemCategoryListForItem(Long itemId) {
        return queryFactory
                .select(itemCategory)
                .from(itemCategory)
                .join(itemCategory.item, item)
                .where(item.id.eq(itemId))
                .fetch();
    }
}
