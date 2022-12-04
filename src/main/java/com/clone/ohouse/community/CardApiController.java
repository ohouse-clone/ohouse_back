package com.clone.ohouse.community;

import com.clone.ohouse.account.SessionUser;
import com.clone.ohouse.community.domain.CardService;
import com.clone.ohouse.community.domain.cardcollections.dto.CardResponseDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestContentDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestHeaderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/card_collections")
@RestController
public class CardApiController {
    private final CardService cardService;


    @PostMapping
    public HttpEntity<Long> save(
            @RequestPart(name = "header") CardSaveRequestHeaderDto headerDto,
            @RequestPart(name = "files") MultipartFile[] multipartFiles,
            @RequestPart(name = "contents") CardSaveRequestContentDto[] contentDtos,
            SessionUser sessionUser) throws Exception{

        if(multipartFiles.length != contentDtos.length) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, sessionUser);

        return new ResponseEntity<>(saveId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public HttpEntity<Long> update(
            @PathVariable Long id,
            @RequestPart(name = "header") CardSaveRequestHeaderDto headerDto,
            @RequestPart(name = "files") MultipartFile[] multipartFiles,
            @RequestPart(name = "contents") CardSaveRequestContentDto[] contentDtos,
            SessionUser sessionUser) throws Exception {
        if(multipartFiles.length != contentDtos.length) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        cardService.update(id, headerDto, multipartFiles, contentDtos, sessionUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id){
        cardService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public CardResponseDto findById(
            @PathVariable Long id){
        CardResponseDto result = cardService.findById(id);

        return result;
    }

//    @GetMapping

}
