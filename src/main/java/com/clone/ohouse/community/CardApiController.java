package com.clone.ohouse.community;

import com.clone.ohouse.account.SessionUser;
import com.clone.ohouse.community.domain.CardService;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.cardcollections.dto.CardBundleViewResponseDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardResponseDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestContentDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestHeaderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/community/api/v1/card_collections")
@RestController
public class CardApiController {
    private final CardService cardService;


    @ApiOperation(
            value = "사진 게시글 등록",
            code = 201,
            notes = "HTTP 양식은 multi-form 방식을 사용해야합니다. 사용되는 키 값으로는 header, files, contents가 있습니다. <br>" +
                    "files와 contents는 배열입니다. 또한 반드시 null이여서는 안됩니다. <br>" +
                    "files는 사진 또는 영상 파일을 의미하며, contents는 sequence(내용글의 위치)와 content(내용 글)를 가진 객체입니다. <br>" +
                    "files와 contents의 배열 개수는 정확하게 서로 같아야합니다. <br>" +
                    "<br>" +
                    "Output은 저장된 게시글의 id입니다." +
                    "<br><br>" +
                    "parameter에 email, name, picture는 무시하세요. 넣어서는 안되는 값입니다."
    )
    @PostMapping
    public HttpEntity<Long> save(
            @ApiParam(name = "key : header", value = "키가 header인 객체입니다.", required = true) @RequestPart(name = "header") CardSaveRequestHeaderDto headerDto,
            @ApiParam(name = "key : files", value = "키가 files인 MediaFile(사진, 동영상)입니다.", required = true) @RequestPart(name = "files") MultipartFile[] multipartFiles,
            @ApiParam(name = "key : contents", value = "키가 contents인 객체입니다.", required = true) @RequestPart(name = "contents") CardSaveRequestContentDto[] contentDtos,
            @ApiParam(hidden = true, value = "무시하세요") SessionUser sessionUser) throws Exception {

        if (multipartFiles.length != contentDtos.length) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, sessionUser);

        return new ResponseEntity<>(saveId, HttpStatus.CREATED);
    }

    @ApiOperation(
            value = "사진 게시글 수정",
            code = 200,
            notes = "save API와 같은 형식을 사용합니다. 단 null값을 허용한다는 점이 다릅니다. <br>" +
                    "HTTP 양식은 multi-form 방식이며 사용되는 키는 header, files, contents가 있습니다. <br>" +
                    "files와 contents는 배열입니다. 또한 반드시 null이여서는 안됩니다. <br>" +
                    "files는 사진 또는 영상 파일을 의미하며, contents는 sequence(내용글의 위치)와 content(내용 글)를 가진 객체입니다. <br>" +
                    "files와 contents의 배열 개수는 정확하게 서로 같아야합니다. <br>" +
                    "save와 다른 점은 files가 null 또는 contents 객체의 properties의 값이 null일 경우, 해당 파트는 업데이트 하지 않습니다. (기존 저장된 값을 유지합니다.) <br>" +
                    "또한 files와 contents 배열의 개수가 기존 게시글의 배열 개수보다 작을 경우, 끝부분부터 현재 배열 개수에 맞게 삭제됩니다. <br>" +
                    "반대로 기존 게시글의 배열의 개수보다 클 경우 새로 추가해서 등록하는데, 이 경우에는 null이 있어서는 안됩니다. 반드시 모든 값이 채워져야 합니다 (신규 save이기 때문에) <br>" +
                    "<br>" +
                    "Output은 없습니다." +
                    "<br><br>" +
                    "parameter에 email, name, picture는 무시하세요. 넣어서는 안되는 값입니다."
    )
    @PutMapping("/{id}")
    public HttpEntity<Long> update(
            @ApiParam(name = "사진 게시글 id", required = true) @PathVariable Long id,
            @ApiParam(name = "key : header", value = "키가 header인 객체입니다.", required = true) @RequestPart(name = "header") CardSaveRequestHeaderDto headerDto,
            @ApiParam(name = "key : files", value = "키가 files인 MediaFile(사진, 동영상)입니다.", required = true) @RequestPart(name = "files") MultipartFile[] multipartFiles,
            @ApiParam(name = "key : contents", value = "키가 contents인 객체입니다.", required = true) @RequestPart(name = "contents") CardSaveRequestContentDto[] contentDtos,
            @ApiParam(hidden = true) SessionUser sessionUser) throws Exception {
        if (multipartFiles.length != contentDtos.length) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        cardService.update(id, headerDto, multipartFiles, contentDtos, sessionUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(
            value = "사진 게시글 삭제",
            code = 200,
            notes = "게시글 id 에 해당하는 사진 게시글을 삭제합니다." +
                    "<br>" +
                    "Output은 없습니다."
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(
            @ApiParam(name = "사진 게시글 id", required = true) @PathVariable Long id) {
        cardService.delete(id);
    }

    @ApiOperation(
            value = "사진 게시글 조회",
            code = 200,
            notes = "게시글 id 에 해당하는 사진 게시글을 조회합니다." +
                    "<br>" +
                    "Output은 CardResponseDto를 반환합니다."
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public CardResponseDto findById(
            @ApiParam(name = "사진 게시글 id", required = true) @PathVariable Long id) {
        CardResponseDto result = cardService.findById(id);

        return result;
    }

    @ApiOperation(
            value = "사진 게시글의 Preview 여러개 조회",
            code = 200,
            notes = "사진 게시글을 Preview Style로 한번에 조회합니다. <br>" +
                    "QueryParameter 형식을 사용해 어떤 방식으로 조회할지 결정할 수 있습니다 <br>" +
                    "housingtype은 다음 중 하나가 허용됩니다. (ONE_ROOM, APARTMENT, VILLA, SINGLE_HOUSE, OFFICE, COMMERCIAL) <br>" +
                    "housestyle은 다음 중 하나가 허용됩니다. (MODERN, NORTH_EUROPE, VINTAGE, ROMANTIC, KOREA_ASIA, UNIQUE, CLASSIC) <br>" +
                    "color는 다음 중 하나가 허용됩니다. (WHIITE, BLACK, BLUE, RED) <br>" +
                    "order는 다음 중 하나가 허용됩니다. (HIT_ASCEND, HIT_DESCEND) <br>" +
                    "Paging 기능을 지원합니다. <br>" +
                    "<br>" +
                    "Output은 CardBundleViewResponseDto를 반환합니다."
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cards")
    public CardBundleViewResponseDto findBundleView(Pageable pageable,
                                                    @ApiParam(value = "key : housingtype") @RequestParam(required = false) HousingType housingtype,
                                                    @ApiParam(value = "key : housestyle") @RequestParam(required = false) HouseStyle housestyle,
                                                    @ApiParam(value = "key : color") @RequestParam(required = false) Color color,
                                                    @ApiParam(value = "key : order") @RequestParam(required = false) SortOrder order) {
        CardSearchCondition condition = new CardSearchCondition(
                housingtype, housestyle, color, order);
//                HousingType.valueOf(housingtype),
//                HouseStyle.valueOf(housestyle),
//                Color.valueOf(color),
//                SortOrder.valueOf(order));

        return cardService.findBundleViewWithContent(pageable, condition);
    }
}
