package com.clone.ohouse;


import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
@Transactional

public class UserTest {
    @Autowired
    UserRepository userRepository;


    @Test
    public void create() throws Exception{
        User user = User.builder()
                .email("qorwltn1234@naver.com")
                .password("12345")
                .nickname("ceta")
                .phone("012")
                .birthday("950721")
                .build();
        User newUser = userRepository.save(user);

    }

    @Test
    public void read() throws Exception{
        Optional<User> user = userRepository.findById(1L);
        user.ifPresent(newUser->{
            System.out.println(newUser);
        });
    }

    @Test
    public void findAll() throws Exception{
        userRepository.findAll();
    }
    @Test
    public void update() throws Exception{
        Optional<User> user = userRepository.findById(1L);
        user.ifPresent(newUser ->{
            newUser.setEmail("new-email.com");
            newUser.setPassword("new_passsword");
            userRepository.save(newUser);
        });
        System.out.println(user);
    }
    @Test
    public void delete() throws Exception{
        Optional<User> user = userRepository.findById(1L);
        user.ifPresent(newUser->{
            userRepository.delete(newUser);
        });
    }
}
