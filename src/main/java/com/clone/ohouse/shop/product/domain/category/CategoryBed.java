package com.clone.ohouse.shop.product.domain.category;

import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CategoryBed {
    private final ItemCategoryCodeRepository itemCategoryCodeRepository;

    public void postCategoryBed(){
        itemCategoryCodeRepository.save(ItemCategoryCode.builder()
                .categoryDetail("가구-침대-침대프레임-일반침대")
                .category1(0)
                .category2(22)
                .category3(20)
                .category4(20)
                .build());

        itemCategoryCodeRepository.save(ItemCategoryCode.builder()
                .categoryDetail("가구-침대-침대프레임-수납침대")
                .category1(0)
                .category2(22)
                .category3(20)
                .category4(21)
                .build());

        itemCategoryCodeRepository.save(ItemCategoryCode.builder()
                .categoryDetail("가구-침대-침대프레임-접이식침대")
                .category1(0)
                .category2(22)
                .category3(20)
                .category4(22)
                .build());
    }
}
