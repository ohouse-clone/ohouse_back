package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.StorePostsService;
import com.clone.ohouse.store.domain.storeposts.StorePostPictures;
import com.clone.ohouse.store.domain.storeposts.StorePostPicturesRepository;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostWithProductsDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsResponseDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsUpdateRequestDto;
import com.clone.ohouse.utility.s3.LocalFileService;
import com.clone.ohouse.utility.s3.S3Service;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Api(
        value = "제품게시글 API"
)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store/api/v1/post")
@RestController
public class StorePostsApiController {
    private final StorePostsService boardService;
    private final S3Service s3Service;
    private final LocalFileService localFileService;
    private final StorePostPicturesRepository storePostPicturesRepository;

    @ApiOperation(
            value = "제품 게시글 등록",
            notes = "판매자가 제품을 등록하기 위해 사용합니다.",
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long save(@RequestBody StorePostsSaveRequestDto saveRequestDto) throws IOException{
        return boardService.save(saveRequestDto);
    }


    @ApiOperation(
            value = "졔품 게시글 image 등록",
            notes = "제품 게시글을 등록하기 전에 image 를 등록합니다.<br>" +
            "제품 게시글 등록할 때, image id가 필요합니다. image id를 해당 API 를 이용햬서 얻을 수 있습니다. <br>" +
            "파일의 순서에 유의하세요. 반드시 content image, preview image 순으로 입력되어야 합니다." +
            "form-data 양식으로 key를 data로 하여 image 파일을 2개 입력하세요<br><br>" +
            "해당 값으로 얻은 id를 게시글 등록에 사용하지 않으면 서버에 파일이 저장되게 됩니다. 따라서 반드시 게시글 등록에 사용해주세요",
            code = 200)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/images")
    public HttpEntity<Long[]> saveImage(
            @ApiParam(name = "data", value = "key data로 2개의 image 파일만 업로드해야 합니다.") @RequestParam("data") MultipartFile[] multipartFiles) throws IOException {
        if(multipartFiles.length != 2) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ArrayList<File> files = new ArrayList<>();
        ArrayList<Long> fileIds = new ArrayList<>();
        try{
            for (MultipartFile multipartFile : multipartFiles) {
                File image = localFileService.createFileBeforeUploadS3(multipartFile, "storepost");
                StorePostPictures picture = storePostPicturesRepository.save(new StorePostPictures(null, null, image.getAbsolutePath()));
                log.debug("save file with name : " + image.getAbsolutePath() + "/" + image.getName());

                files.add(image);
                fileIds.add(picture.getId());
            }
        }
        catch (Exception e){
            log.debug("Fail to save StorePost images");
            for (File file : files) {
                localFileService.deleteFile(file);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(fileIds.toArray(new Long[fileIds.size()]), HttpStatus.OK);
    }

    @ApiOperation(
            value = "제품 게시글 수정",
            notes = "판매자 혹은 관리자가 게시글 수정에 사용합니다.",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)"),
            @ApiImplicitParam(name = "updateRequestDto", value = "게시글 update dto")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody StorePostsUpdateRequestDto updateRequestDto) throws IOException{
        boardService.update(id, updateRequestDto);
    }

    @ApiOperation(
            value = "제품 게시글 얻기",
            notes = "게시글에 대한 모든 내용을 가져옵니다. (title, content, 작성자, 수정일시, 수정자 등)",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ApiImplicitParam(name = "id", value = "제품 게시글의 id (제품 id X)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id){
        System.out.println("Call delete");
        return boardService.delete(id);
    }

    @ApiOperation(
            value = "게시글 조회 (with products)",
            notes = "게시글을 조회합니다. 연관된 product를 모두 가져옵니다",
            code = 200
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/productswith/{id}")
    public StorePostWithProductsDto findByIdWIthProduct(
           @ApiParam(name = "storepost id") @PathVariable Long id) throws Exception{
        return boardService.findByIdWithProduct(id);
    }

}
