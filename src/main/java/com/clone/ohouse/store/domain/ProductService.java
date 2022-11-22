package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRepository;
import com.clone.ohouse.store.domain.product.ProductRepository;
import com.clone.ohouse.store.domain.product.ProductSearchCondition;
import com.clone.ohouse.store.domain.product.dto.*;
import com.clone.ohouse.store.domain.product.Product;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final StorePostsRepository storePostsRepository;

    public Long save(ProductSaveRequestDto requestDto) throws Exception {
        Item item = itemRepository.findById(requestDto.getItemId()).orElseThrow(() -> new NoSuchElementException("item id가 잘못되었습니다."));
        StorePosts post = null;
        if(requestDto.getStorePostId() != null)
            post = storePostsRepository.findById(requestDto.getStorePostId()).orElseThrow(() -> new NoSuchElementException("StorePost id가 잘못되었습니다. : " + requestDto.getStorePostId()));


        return productRepository.save(
                Product.builder()
                        .item(item)
                        .productName(requestDto.getProductName())
                        .price(requestDto.getPrice())
                        .stock(requestDto.getStock())
                        .rateDiscount(requestDto.getRateDiscount())
                        .popular(0L)
                        .storePosts(post)
                        .build()
        ).getId();
    }


    public void update(Long productId, ProductUpdateRequestDto requestDto) throws Exception{
        Item item = null;
        StorePosts post = null;

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Product : " + productId));
        if(requestDto.getItemId() != null) item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 item : " + requestDto.getItemId()));
        if(requestDto.getStorePostId() != null) post = storePostsRepository.findById(requestDto.getStorePostId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 post id : " + requestDto.getStorePostId()));

        product.update(
                item,
                requestDto.getProductName(),
                requestDto.getStock(),
                requestDto.getPrice(),
                requestDto.getRateDiscount(),
                post);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDetailDto findByIdWithFetchJoin(Long id) throws Exception{
        Product product = productRepository.findByIdWithFetchJoin(id).orElseThrow(() -> new NoSuchElementException("잘못된 product id : " + id));

        return new ProductDetailDto(product);
    }

    public ProductListResponseDto findByItemWithProductCondition(Pageable pageable,Long itemId, ProductSearchCondition productSearchCondition){
        //TODO: Working on
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 item id : " + itemId));

        Long total = productRepository.countByItemId(itemId, productSearchCondition);
        List<Product> products = productRepository.findByItemId(pageable, itemId, productSearchCondition);

        List<ProductResponseDto> list = products.stream().map((p)->new ProductResponseDto(p)).collect(Collectors.toCollection(ArrayList<ProductResponseDto>::new));

        return new ProductListResponseDto(total, Long.valueOf(list.size()), list);
    }



}
