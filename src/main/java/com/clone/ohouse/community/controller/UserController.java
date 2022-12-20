package com.clone.ohouse.community.controller;

import com.clone.ohouse.community.dto.UserDto;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.entity.UserCreateForm;
import com.clone.ohouse.community.repository.UserRepository;
import com.clone.ohouse.community.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.logging.Logger.*;
import javax.validation.Valid;
import java.awt.*;
import java.util.Optional;
@Log
@RestController

public class UserController {
    @Autowired
    UserService userService;
    UserRepository userRepository;


    @GetMapping("/login")
    public User signIn(@RequestBody User requestbody) {
        User user = User.builder()
                .email(requestbody.getEmail())
                .password(requestbody.getPassword())
                .nickname(requestbody.getNickname())
                .phone(requestbody.getPhone())
                .birthday(requestbody.getBirthday())
                .build();
        userService.save(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional <User> user = userRepository.findById(id);

        userService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/update")
    public void update(@RequestBody User requestbody) {
        //email로 검색 따로 구현
        Long newId = requestbody.getId();
        userService.update(newId);
    }

    @GetMapping("/users")
    public void getAllUser(@RequestBody User requestbody) {
        userService.findAll();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
