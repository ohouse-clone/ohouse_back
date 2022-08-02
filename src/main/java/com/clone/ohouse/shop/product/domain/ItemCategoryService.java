package com.clone.ohouse.shop.product.domain;

import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.dto.ItemCategoryListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemCategoryService {
    private final ItemCategoryCodeRepository itemCategoryCodeRepository;

    @Transactional
    public List<ItemCategoryListResponseDto> getItemCategoryCodeListAll(){

        return itemCategoryCodeRepository.findDistinctAllAsc()
                .stream().map(ItemCategoryListResponseDto::new)
                .collect(Collectors.toList());
    }
}
