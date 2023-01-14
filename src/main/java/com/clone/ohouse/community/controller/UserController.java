package com.clone.ohouse.community.controller;

import com.clone.ohouse.community.dto.UserCreateDTO;
import com.clone.ohouse.community.dto.UserListResponseDTO;
import com.clone.ohouse.community.dto.UserModifyDTO;
import com.clone.ohouse.community.dto.UserResponseDTO;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import com.clone.ohouse.community.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// 리소스 : 사용자 (User)
/*
     사용자 목록 조회:  /users       - GET
     게시물 개별 조회:  /users/api/{id}  - GET
     게시물 등록:      /users       - POST
     게시물 수정:      /users/api/{id}  - PATCH
     게시물 삭제:      /users/api/{id}  - DELETE
 */

@ApiResponses({
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@Slf4j
@RestController
@RequestMapping("/users/api")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;
    // 사용자 등록
    @ApiOperation(
            value = "사용자(User) 생성",
            notes = "사용자를 생성한다"
    )
    @GetMapping
    public ResponseEntity<?> create(@RequestBody UserCreateDTO createDTO){
        log.info("/users/api GET request");
        log.info("사용자 정보 : {}",createDTO);
        User user = createDTO.toEntity();
        boolean flag = userService.save(user);
        return flag
                ? ResponseEntity.ok().body("INSERT-SUCCESS")
                : ResponseEntity.badRequest().body("INSERT-FAIL");
    }
    @ApiOperation(
            value = "사용자(User) 삭제",
            notes = "사용자를 삭제한다"
    )
    // 사용자 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@ApiParam (value = "user id") @PathVariable Long id) {
        log.info("/users/api/{} DELETE request",id);
        boolean flag = userService.delete(id);
        return flag
                ? ResponseEntity.ok().body("DELETE-SUCCESS")
                : ResponseEntity.badRequest().body("DELETE-FAIL");
    }
    @ApiOperation(
            value = "사용자(User) 수정",
            notes = "사용자를 수정한다"
    )
    // 사용자 수정
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<?> modify(@ApiParam(value = "user id") @PathVariable Long id
    , @RequestBody UserModifyDTO userModifyDTO) {
        // 수정 전 데이터 조회
        User user = userService.findById(id);
        boolean flag = userService.update(userModifyDTO, id);
        return flag
                ? ResponseEntity.ok().body("UPDATE-SUCCESS")
                : ResponseEntity.badRequest().body("UPDATE-FAIL");
    }

    // 모든 사용자 조회
    @ApiOperation(
            value = "모든 사용자(User) 조회",
            notes = "모든 사용자를 조회한다"
    )
    @GetMapping
    public ResponseEntity<?> getAllUser() {
        log.info("/users/api GET request");
        List<User> userList = userService.findAll();
        List<UserResponseDTO> responseDTOList = userList.stream()

                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        UserListResponseDTO listResponseDTO = UserListResponseDTO.builder()
                .count(responseDTOList.size())
                .users(responseDTOList)
                .build();
        return ResponseEntity
                .ok()
                .body(listResponseDTO);
    }
    // 개별 사용자 조회
    @ApiOperation(
            value = "개별 사용자(User) 조회",
            notes = "개별 사용자를 조회한다"
    )
    @GetMapping("{id}")
    public ResponseEntity<?> getOneUser(@ApiParam(value = "user id") @PathVariable Long id){
        log.info("/users/api/{} GET requests");
        User user = userService.findById(id);
        UserResponseDTO dto = new UserResponseDTO(user);
        return ResponseEntity
                .ok()
                .body(dto);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
