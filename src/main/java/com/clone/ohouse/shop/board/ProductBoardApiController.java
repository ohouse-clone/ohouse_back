package com.clone.ohouse.shop.board;

import com.clone.ohouse.shop.board.domain.ProductBoardService;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardResponseDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardSaveRequestDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductBoardApiController {
    private final ProductBoardService boardService;

    @PostMapping("/store/productions")
    public Long save(@RequestBody ProductBoardSaveRequestDto saveRequestDto){
        return boardService.save(saveRequestDto);
    }

    @PutMapping("/store/productions/{id}")
    public Long update(@PathVariable Long id, ProductBoardUpdateRequestDto updateRequestDto){
        return boardService.update(id, updateRequestDto);
    }

    @GetMapping("/store/productions/{id}")
    public ProductBoardResponseDto findById(@PathVariable Long id){
        return boardService.findById(id);
    }

    @DeleteMapping("/store/productions/{id}")
    public Long delete(@PathVariable Long id){
        return boardService.delete(id);
    }

}
