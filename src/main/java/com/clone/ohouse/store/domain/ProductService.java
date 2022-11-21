package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.dto.ProductAllListResponseDto;
import com.clone.ohouse.store.domain.product.dto.ProductSaveRequestDto;
import com.clone.ohouse.store.domain.product.dto.ProductUpdateRequestDto;
import com.clone.ohouse.store.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    public Long save(ProductSaveRequestDto requestDto) throws Exception {
        Item item = itemRepository.findById(requestDto.getItemId()).orElseThrow(() -> new NoSuchElementException("item id가 잘못되었습니다."));

        return productRepository.save(
                new Product(
                        item,
                        requestDto.getProductName(),
                        requestDto.getPrice(),
                        requestDto.getStock(),
                        requestDto.getRateDiscount(),
                        0L)
        ).getId();
    }


    public void update(Long productId, ProductUpdateRequestDto requestDto) throws Exception{
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Product : " + productId));
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 item : " + requestDto.getItemId()));

        product.update(
                item,
                requestDto.getProductName(),
                requestDto.getStock(),
                requestDto.getPrice(),
                requestDto.getRateDiscount());
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void findById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("잘못된 product id : " + id));
    }


    @Transactional(readOnly = true)
    public List<ProductAllListResponseDto> findAllAsc() {
        return productRepository.findAll().stream()
                .map(ProductAllListResponseDto::new)
                .collect(Collectors.toList());
    }



}
