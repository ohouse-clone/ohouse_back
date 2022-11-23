package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ProductService;
import com.clone.ohouse.store.domain.product.ProductSearchCondition;
import com.clone.ohouse.store.domain.product.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RequestMapping("/store/api/v1/product")
@RestController
public class ProductApiController {
    private final ProductService productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public Long save(
            @RequestBody ProductSaveRequestDto requestDto) throws Exception{
        return productService.save(requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void update(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequestDto requestDto) throws Exception{
        productService.update(id, requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id){
        productService.delete(id);
    }

    @GetMapping("/{id}")
    public HttpEntity<ProductDetailDto> findById(
            @PathVariable Long id) throws Exception {
        ProductDetailDto result = null;
        try{
            result = productService.findByIdWithFetchJoin(id);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/products")
    public HttpEntity<ProductListResponseDto> findByItemIdWithProductCondition(
            @RequestParam Long itemId,
            Pageable pageable,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer priceBegin,
            @RequestParam(required = false) Integer priceEnd,
            @RequestParam(required = false) Integer stockBegin,
            @RequestParam(required = false) Long popularBegin
    ) throws Exception {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .productName(productName)
                .priceBegin(priceBegin)
                .priceEnd(priceEnd)
                .stockBegin(stockBegin)
                .popularBegin(popularBegin)
                .build();
        ProductListResponseDto result = null;
        try {
            result = productService.findByItemWithProductCondition(pageable, itemId, productSearchCondition);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/products")
    public HttpEntity registerStorePost(
            @RequestBody ProductStorePostIdUpdateRequestDto requestDto) {
        try {
            productService.updateWithStorePostId(requestDto);
        } catch (Exception e){
            //TODO: Logger 필요
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
