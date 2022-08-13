package com.clone.ohouse.shop.product.domain;

import com.clone.ohouse.shop.product.domain.access.ItemRepository;
import com.clone.ohouse.shop.product.domain.dto.ItemAllListResponseDto;
import com.clone.ohouse.shop.product.domain.dto.ItemSaveRequestDto;
import com.clone.ohouse.shop.product.domain.dto.ItemUpdateRequestDto;
import com.clone.ohouse.shop.product.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Long save(ItemSaveRequestDto requestDto){
        return itemRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long seq, ItemUpdateRequestDto requestDto) {
        Item posts = itemRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Item : " + seq));

        posts.update(requestDto.getName(), requestDto.getModelName(), requestDto.getBrandName());

        return seq;
    }

    @Transactional(readOnly = true)
    public List<ItemAllListResponseDto> findAllAsc(){
        return itemRepository.findAllAsc().stream()
                .map(ItemAllListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long seq){
        Item item = itemRepository.findById(seq)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 Item : " + seq));

        itemRepository.delete(item);
    }
}
