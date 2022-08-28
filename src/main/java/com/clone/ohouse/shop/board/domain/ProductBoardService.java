package com.clone.ohouse.shop.board.domain;


import com.clone.ohouse.shop.board.domain.access.ProductBoardRepository;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardResponseDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardSaveRequestDto;
import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ProductBoardService {
    private final ProductBoardRepository productBoardRepository;

    @Transactional
    public Long save(ProductBoardSaveRequestDto saveRequestDto){
        return productBoardRepository.save(saveRequestDto.toEntity()).getId();
    }

    @Transactional
    public void delete(Long id){
        productBoardRepository.deleteById(id);
    }

    @Transactional
    public ProductBoardResponseDto findById(Long id){
        ProductBoard entity = productBoardRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        return new ProductBoardResponseDto(entity);
    }
}
