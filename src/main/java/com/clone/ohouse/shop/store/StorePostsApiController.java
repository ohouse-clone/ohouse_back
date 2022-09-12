package com.clone.ohouse.shop.store;

import com.clone.ohouse.shop.store.domain.StorePostsService;
import com.clone.ohouse.shop.store.domain.dto.StorePostsResponseDto;
import com.clone.ohouse.shop.store.domain.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.shop.store.domain.dto.StorePostsUpdateRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class StorePostsApiController {
    private final StorePostsService boardService;

    @ApiOperation(
            value = "제품 게시글 등록",
            notes = "판매자가 제품을 등록하기 위해 사용합니다.",
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/store/productions")
    public void save(@RequestBody StorePostsSaveRequestDto saveRequestDto) {
        boardService.save(saveRequestDto);
    }

    @ApiOperation(
            value = "제품 게시글 수정",
            notes = "판매자 혹은 관리자가 게시글 수정에 사용합니다.",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)"),
            @ApiImplicitParam(name = "updateRequestDto", value = " df dfsf")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/store/productions/{id}")
    public void update(@PathVariable Long id, @RequestBody StorePostsUpdateRequestDto updateRequestDto) {
        boardService.update(id, updateRequestDto);
    }

    @ApiOperation(
            value = "제품 게시글 얻기",
            notes = "게시글에 대한 모든 내용을 가져옵니다. (title, content, 작성자, 수정일시, 수정자 등)",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/store/productions/{id}")
    public StorePostsResponseDto findById(@PathVariable Long id) {
        return boardService.findById(id);
    }
    @ApiOperation(
            value = "제품 게시글 삭제",
            notes = "게시글을 삭제합니다.",
            code = 204)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/store/productions/{id}")

    public Long delete(@PathVariable Long id){
        System.out.println("Call delete");
        return boardService.delete(id);
    }

}
