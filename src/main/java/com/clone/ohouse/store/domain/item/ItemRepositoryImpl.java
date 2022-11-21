package com.clone.ohouse.store.domain.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.clone.ohouse.store.domain.category.QItemCategory.itemCategory;
import static com.clone.ohouse.store.domain.item.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> findByCategory(Long categoryId, Pageable pageable, ItemSearchCondition itemSearchCondition) {
        List<Item> result = queryFactory
                .select(item)
                .from(item)
                .join(item.itemCategories, itemCategory)
                .where(itemCategory.category.id.eq(categoryId),
                        itemSearchCondition.eqItemName(),
                        itemSearchCondition.eqBrandName(),
                        itemSearchCondition.eqModelName())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return result;
    }

    @Override
    public Long findTotalNumByCategory(Long categoryId, ItemSearchCondition itemSearchCondition) {
        return queryFactory
                .select(item.count())
                .from(item)
                .join(item.itemCategories, itemCategory)
                .where(itemCategory.category.id.eq(categoryId),
                        itemSearchCondition.eqItemName(),
                        itemSearchCondition.eqBrandName(),
                        itemSearchCondition.eqModelName())
                .fetchOne();
    }
}
