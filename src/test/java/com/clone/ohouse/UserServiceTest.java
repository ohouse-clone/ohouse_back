package com.clone.ohouse;


import com.clone.ohouse.community.dto.LoginResponseDTO;
import com.clone.ohouse.community.dto.UserCreateDTO;
import com.clone.ohouse.community.dto.UserCreateResponseDTO;
import com.clone.ohouse.community.service.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("중복된 이메일이 포함된 회원정보로 가입하면 RuntimeException이 발생해야 한다.")
    void validateEmailTest() {
        // given
        UserCreateDTO dto = UserCreateDTO.builder()
                .email("abc1234@def.com")
                .password("54543")
                .nickname("키키킥")
                .build();

        //when
        //then
        assertThrows(RuntimeException.class, () -> {
            userService.create(dto);
        });
    }
    @Test
    @DisplayName("검증된 회원정보로 가입하면 회원가입에 성공해야 한다.")
    void createTest() {
        // given
        UserCreateDTO dto = UserCreateDTO.builder()
                .email("zzzz9878@def.com")
                .password("1234")
                .nickname("암호맨")
                .build();

        //when
        UserCreateResponseDTO responseDTO = userService.create(dto);
        //then
        System.out.println("responseDTO = " + responseDTO);
        assertEquals("암호맨", responseDTO.getNickname());

    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인을 시도하면 Exception이 발생해야한다.")
    void noUserTest() {
        //given
        String email = "dsfdsff@aasad.com";
        String password = "1234";

        //then
        assertThrows(RuntimeException.class, () -> {

            //when
            userService.getByCredentials(email, password);
        });
    }

    @Test
    @DisplayName("틀린 비밀번호로 로그인을 시도하면 Exception이 발생해야한다.")
    void invalidPasswordTest() {
        //given
        String email = "postman@naver.com";
        String password = "hdkljsfkjhdskhjsdf";

        //then
        assertThrows(RuntimeException.class, () -> {

            //when
            userService.getByCredentials(email, password);
        });
    }
    @Test
    @DisplayName("정확한 정보로 로그인을 시도하면 회원정보가 반환되어야 한다.")
    void loginTest() {
        //given
        String email = "abc1234@aaa.com";
        String password = "abc1234!";

        //when
        LoginResponseDTO loginUser = userService.getByCredentials(email, password);
        //then
        assertEquals("야호", loginUser.getNickName());

        System.out.println(loginUser);
    }


}
