package com.clone.ohouse.community.domain.cardcollections;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CardRepositoryCustom {
    Optional<Card> findByIdWithContent(Long id);

    List<Card> findBundleViewWithContent(Pageable pageable, CardSearchCondition cardSearchCondition);
    Long findBundleViewCount(CardSearchCondition cardSearchCondition);
}
