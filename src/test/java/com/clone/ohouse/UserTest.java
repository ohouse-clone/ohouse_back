package com.clone.ohouse;


import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional

public class UserTest {
    @Autowired
    UserRepository userRepository;


    @Test
    public void create() throws Exception {
        User user = User.builder()
                .email("qorwltn1234@naver.com")
                .password("12345")
                .nickname("ceta")
                .phone("012")
                .birthday("950721")
                .build();
        //저장
        User newUser = userRepository.save(user);
        //조회
        User result = userRepository.findById(user.getId()).get();
        Optional<User> findUser = userRepository.findById(user.getId());

        assertThat(findUser.get().getId()).isEqualTo(user.getId());
        assertThat(findUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(newUser).isEqualTo(result);
        assertThat(newUser.getId()).isEqualTo(result.getId());

    }

    @Test
    public void CRUD() throws Exception {
        User user1 = User.builder()
                .email("bjs_07@gmail.com")
                .password("pass1")
                .nickname("cetta")
                .phone("012-3456")
                .birthday("950722")
                .build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .password("pass2")
                .nickname("junu")
                .phone("123-4567")
                .birthday("900716")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        User findUser1 = userRepository.findById(user1.getId()).get();
        User findUser2 = userRepository.findById(user2.getId()).get();
       assertThat(findUser1).isEqualTo(user1);
       assertThat(findUser2).isEqualTo(user2);

       List<User> users = userRepository.findAll();
       long count = userRepository.count();

       assertThat(users.size()).isEqualTo(count);
       assertThat(users.size()).isEqualTo(2);

       userRepository.delete(user1);
       userRepository.delete(user2);
       long deletedCount = userRepository.count();
       assertThat(deletedCount).isEqualTo(0);
    }

}
