package com.clone.ohouse.shop.product.domain;

import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.dto.ProductAllListResponseDto;
import com.clone.ohouse.shop.product.domain.dto.ProductSaveRequestDto;
import com.clone.ohouse.shop.product.domain.dto.ProductUpdateRequestDto;
import com.clone.ohouse.shop.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;


    @Transactional
    public Long save(ProductSaveRequestDto requestDto) {
        return productRepository.save(requestDto.toEntity()).getId();
    }


    @Transactional
    public Long update(Long seq, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Product : " + seq));


        product.update(
                requestDto.getItem(),
                requestDto.getProductName(),
                requestDto.getStock(),
                requestDto.getPrice(),
                requestDto.getRateDiscount(),
                requestDto.getSize(),
                requestDto.getColor());

        return seq;
    }


    @Transactional(readOnly = true)
    public List<ProductAllListResponseDto> findAllAsc() {
        return productRepository.findAll().stream()
                .map(ProductAllListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long seq) {

        productRepository.deleteById(seq);
    }

}
