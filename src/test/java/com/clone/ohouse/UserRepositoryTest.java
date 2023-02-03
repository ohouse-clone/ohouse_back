package com.clone.ohouse;


import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입에 성공해야 한다.")
    @Transactional
    @Rollback
    void saveTest() {
        //given
        User user = new User();
        user.setNickname("말똥이");
        user.setEmail("abc1234@def.com");
        user.setPassword("1234");

        //when
        User savedUser = userRepository.save(user);
        //then
        assertNotNull(savedUser);
    }
}
