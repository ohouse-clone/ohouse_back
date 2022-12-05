package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ProductService;
import com.clone.ohouse.store.domain.product.ProductSearchCondition;
import com.clone.ohouse.store.domain.product.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Api(
        value = "product를 등록, 제거, 조회에 대한 API"
)
@RequiredArgsConstructor
@RequestMapping("/store/api/v1/product")
@RestController
public class ProductApiController {
    private final ProductService productService;

    @ApiOperation(
            value = "product 저장",
            code = 201
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long save(
            @RequestBody ProductSaveRequestDto requestDto) throws Exception{
        return productService.save(requestDto);
    }

    @ApiOperation(
        value = "product 수정",
        code = 200
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void update(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequestDto requestDto) throws Exception{
        productService.update(id, requestDto);
    }

    @ApiOperation(
            value = "product 삭제",
            code = 200
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(
            @ApiParam(value = "product id") @PathVariable Long id){
        productService.delete(id);
    }

    @ApiOperation(
            value = "product 찾기",
            code = 200
    )
    @GetMapping("/{id}")
    public HttpEntity<ProductDetailDto> findById(
            @ApiParam(value = "product id") @PathVariable Long id) throws Exception {
        ProductDetailDto result = null;
        try{
            result = productService.findByIdWithFetchJoin(id);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(
            value = "특정 item 하위의 product 조회",
            notes = "productName, price x~x, stock x~, popular x~ 에 대해서 조건을 줄 수 있습니다. <br>" +
                    "예를 들어서 priceBegin 은 특정 인수값 이상, 그리고 priceEnd는 이하의 조건을 가지는 특정 item 하위의 모든 product를 조회합니다.",
            code = 200
    )
    @GetMapping("/products")
    public HttpEntity<ProductListResponseDto> findByItemIdWithProductCondition(
            @ApiParam(required = true) @RequestParam Long itemId,
            Pageable pageable,
            @ApiParam(value = "찾으려는 제품이름",required = false) @RequestParam(required = false) String productName,
            @ApiParam(value = "price value 이상 조건",required = false) @RequestParam(required = false) Integer priceBegin,
            @ApiParam(value = "price value 이하 조건",required = false) @RequestParam(required = false) Integer priceEnd,
            @ApiParam(value = "stock value 이상 조건",required = false) @RequestParam(required = false) Integer stockBegin,
            @ApiParam(value = "popular value 이상 조건",required = false) @RequestParam(required = false) Long popularBegin
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
    @ApiOperation(
            value = "storepost에 product를 등록",
            notes = "특정 post에 products를 등록합니다. products는 Long type array입니다",
            code = 200
    )
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
