package com.clone.ohouse;


import com.clone.ohouse.community.dto.UserCreateDTO;
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
}
