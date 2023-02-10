package com.clone.ohouse.community.controller;

import com.clone.ohouse.community.dto.*;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.service.UserService;
import com.clone.ohouse.exception.DuplicatedEmailException;
import com.clone.ohouse.exception.NoRegisteredArgumentsException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    // 모든 사용자 조회
    @ApiOperation(
            value = "모든 사용자(User) 조회",
            notes = "모든 사용자를 조회한다"
    )
    @GetMapping
    public ResponseEntity<?> getAllUser() {
        log.info("/users/api GET request");
        UserListResponseDTO responseDTO = userService.findAll();
        return ResponseEntity
                .ok()
                .body(responseDTO);
    }

    // 개별 사용자 조회
    @ApiOperation(
            value = "개별 사용자(User) 조회",
            notes = "개별 사용자를 조회한다"
    )
    @GetMapping("{id}")
    public ResponseEntity<?> getOneUser(@ApiParam(value = "user id") @PathVariable Long id){
        log.info("/users/api/{} GET requests");
        Optional<User> user = userService.findById(id);
        UserCreateResponseDTO dto = new UserCreateResponseDTO(user);
        return ResponseEntity
                .ok()
                .body(dto);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ApiOperation(
            value = "회원가임 요청",
            notes = "회원 가입을 요청한다"
    )
    // 회원가입 요청 처리
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Validated @RequestBody UserCreateDTO createDTO
            , BindingResult result
    ) {
        log.info("/api/auth/signup POST! - {}", createDTO);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.toString());
        }

        try {
            UserCreateResponseDTO responseDTO
                    = userService.create(createDTO);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (NoRegisteredArgumentsException e) {
            // 예외 상황 2가지 (dto가 null인 문제, 이메일 중복문제)
            log.warn("필수 가입 정보를 다시확인하세요.");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (DuplicatedEmailException e) {
            log.warn("중복되었습니다. 다른 이메일을 작성해주세요.");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    // 이메일 중복확인 요청 처리
    // GET : /api/auth/check?email=abc@bbb.com
    @GetMapping("/check")
    public ResponseEntity<?> checkEmail(String email) {
        if (email == null || email.trim().equals("")) {
            return ResponseEntity.badRequest().body("이메일을 전달해 주세요");
        }
        boolean flag = userService.isDuplicate(email);
        log.info("{} 중복 여부?? - {}", email, flag);
        return ResponseEntity.ok().body(flag);
    }

    // 로그인 요청 처리
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @Validated @RequestBody LoginRequestDTO requestDTO) {

        try {
            LoginResponseDTO userInfo = userService.getByCredentials(
                    requestDTO.getEmail(),
                    requestDTO.getPassword()
            );
            return ResponseEntity
                    .ok()
                    .body(userInfo);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(LoginResponseDTO.builder()
                            .message(e.getMessage())
                            .build()
                    );
        }
    }
}
