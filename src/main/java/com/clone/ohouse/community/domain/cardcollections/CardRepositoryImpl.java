package com.clone.ohouse.community.domain.cardcollections;

import com.clone.ohouse.community.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.clone.ohouse.community.domain.QPost.*;
import static com.clone.ohouse.community.domain.cardcollections.QCard.*;
import static com.clone.ohouse.community.domain.cardcollections.QCardContent.*;
import static com.clone.ohouse.community.domain.cardcollections.QCardMediaFile.*;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Card> findByIdWithContent(Long id) {
        Card entity = queryFactory
                .select(card)
                .from(card)
                .leftJoin(card.cardContents, cardContent).fetchJoin()
                .leftJoin(cardContent.cardMediaFile, cardMediaFile).fetchJoin()
                .where(card.id.eq(id))
                .fetchOne();


        return Optional.ofNullable(entity);
    }

    @Override
    public List<Card> findBundleViewWithContent(Pageable pageable, CardSearchCondition cardSearchCondition) {
        return queryFactory
                .select(card)
                .from(card)
                .leftJoin(card.cardContents, cardContent).fetchJoin()
                .join(cardContent.cardMediaFile, cardMediaFile).fetchJoin()
                .where(cardSearchCondition.eqHouseStyle(), cardSearchCondition.eqHosingType(), cardSearchCondition.eqColor())
                .orderBy(cardSearchCondition.orderByHit())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long findBundleViewCount(CardSearchCondition cardSearchCondition) {
        return queryFactory
                .select(card.countDistinct())
                .from(card)
                .join(card.cardContents, cardContent)
                .join(cardContent.cardMediaFile, cardMediaFile)
                .where(cardSearchCondition.eqHouseStyle(), cardSearchCondition.eqHosingType(), cardSearchCondition.eqColor())
                .fetchOne();
    }
}
