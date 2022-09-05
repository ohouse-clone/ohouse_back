package com.clone.ohouse.shop.product.domain;

import com.clone.ohouse.shop.product.domain.access.FurnitureRepository;
import com.clone.ohouse.shop.product.domain.dto.FurnitureAllListResponseDto;
import com.clone.ohouse.shop.product.domain.dto.FurnitureSaveRequestDto;
import com.clone.ohouse.shop.product.domain.dto.FurnitureUpdateRequestDto;
import com.clone.ohouse.shop.product.domain.entity.Furniture;
import com.clone.ohouse.shop.product.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class FurnitureService {
    private final FurnitureRepository furnitureRepository;

    @Transactional
    public Long save(FurnitureSaveRequestDto requestDto){
        return furnitureRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long seq, FurnitureUpdateRequestDto requestDto) {
        Furniture posts = furnitureRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Item : " + seq));

        posts.update( requestDto.getCategoryCode(),requestDto.getName(), requestDto.getModelName(), requestDto.getBrandName(), requestDto.getSize(), requestDto.getColor());

        return seq;
    }

    @Transactional(readOnly = true)
    public List<FurnitureAllListResponseDto> findAllAsc(){
        return furnitureRepository.findAllAsc().stream()
                .map(FurnitureAllListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long seq){
        Furniture item = furnitureRepository.findById(seq)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 Item : " + seq));

        furnitureRepository.delete(item);
    }
}
