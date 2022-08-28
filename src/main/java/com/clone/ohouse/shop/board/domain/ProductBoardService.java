package com.clone.ohouse.shop.board.domain;


import com.clone.ohouse.shop.board.domain.access.ProductBoardRepository;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardResponseDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardSaveRequestDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardUpdateRequestDto;
import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductBoardService {
    private final ProductBoardRepository productBoardRepository;

    @Transactional
    public Long save(ProductBoardSaveRequestDto saveRequestDto){
        return productBoardRepository.save(saveRequestDto.toEntity()).getId();
    }

    @Transactional
    public Long delete(Long id){
        productBoardRepository.deleteById(id);

        return id;
    }

    @Transactional
    public Long update(Long id, ProductBoardUpdateRequestDto dto){
        ProductBoard entity = productBoardRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        entity.update(dto.isActive(), dto.getTitle(), dto.getContent().getBytes(StandardCharsets.UTF_8), dto.getModifiedUser(), dto.isDeleted());

        return productBoardRepository.save(entity).getId();
    }

    @Transactional
    public ProductBoardResponseDto findById(Long id){
        ProductBoard entity = productBoardRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        return new ProductBoardResponseDto(entity);
    }


}
