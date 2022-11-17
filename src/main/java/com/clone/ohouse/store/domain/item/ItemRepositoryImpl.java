package com.clone.ohouse.store.domain.item;

import com.clone.ohouse.store.domain.category.CategorySearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> findByCategory(CategorySearch categorySearch) {
        //TODO
        return null;
    }
}
