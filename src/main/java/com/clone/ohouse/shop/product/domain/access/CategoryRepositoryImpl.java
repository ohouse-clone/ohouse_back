package com.clone.ohouse.shop.product.domain.access;


import com.clone.ohouse.shop.product.domain.dto.CategoryIdsDto;
import com.clone.ohouse.shop.product.domain.entity.Category;
import com.clone.ohouse.shop.product.domain.entity.QCategory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.clone.ohouse.shop.product.domain.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    @Override
    public Category findCategory(CategorySearch condition) {

        QCategory category1 = new QCategory("c1");
        QCategory category2 = new QCategory("c2");
        QCategory category3 = new QCategory("c3");
        QCategory category4 = new QCategory("c4");

        Category result = null;
        if(condition.code2 == null){
            result = queryFactory
                    .selectFrom(category1)
                    .where(
                            category1.parent.isNull()
                                    .and(category1.code.eq(condition.code1)))
                    .fetchOne();
        }
        else if(condition.code3 == null){
            result = queryFactory
                    .select(category2)
                    .from(category2)
                    .join(category2.parent, category1)
                    .where(
                            category1.parent.isNull()
                                    .and(category1.code.eq(condition.code1))
                                    .and(category2.code.eq(condition.code2)))
                    .fetchOne();
        }
        else if(condition.code4 == null){
            result = queryFactory
                    .select(category3)
                    .from(category3)
                    .join(category3.parent, category2)
                    .join(category2.parent, category1)
                    .where(
                            category1.parent.isNull()
                                    .and(category1.code.eq(condition.code1))
                                    .and(category2.code.eq(condition.code2))
                                    .and(category3.code.eq(condition.code3))
                    )
                    .fetchOne();
        }
        else {
            result = queryFactory
                    .select(category4)
                    .from(category4)
                    .join(category4.parent, category3)
                    .join(category3.parent, category2)
                    .join(category2.parent, category1)
                    .where(
                            category1.parent.isNull()
                                    .and(category1.code.eq(condition.code1))
                                    .and(category2.code.eq(condition.code2))
                                    .and(category3.code.eq(condition.code3))
                                    .and(category4.code.eq(condition.code4)))
                    .fetchOne();
        }

        return result;
    }

    @Override
    public List<Category> findCategories(CategorySearch condition) {
        List<Category> result = new ArrayList<>();
        List<QCategory> qCategories = new ArrayList<>();
        QCategory category1 = new QCategory("c1");
        QCategory category2 = new QCategory("c2");
        QCategory category3 = new QCategory("c3");
        QCategory category4 = new QCategory("c4");

        qCategories.add(category1);
        qCategories.add(category2);
        qCategories.add(category3);
        qCategories.add(category4);

        Tuple tuple = queryFactory
                .select(
                        category1,
                        category2,
                        category3,
                        category4
                )
                .from(category4)
                .join(category4.parent, category3)
                .join(category3.parent, category2)
                .join(category2.parent, category1)
                .where(
                        category1.parent.isNull()
                                .and(category1.code.eq(condition.code1))
                                .and(category2.code.eq(condition.code2))
                                .and(category3.code.eq(condition.code3))
                                .and(category4.code.eq(condition.code4)))
                .fetchOne();

        for(int i = 0; i < Category.CATEGORY_SIZE; ++i)
            result.add(tuple.get(qCategories.get(i)));
        return result;
    }
}
