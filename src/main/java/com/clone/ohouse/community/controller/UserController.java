package com.clone.ohouse.community.controller;

import com.clone.ohouse.community.dto.UserDto;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.entity.UserCreateForm;
import com.clone.ohouse.community.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;
import java.util.logging.Logger.*;
import javax.validation.Valid;
import java.awt.*;
import java.util.Optional;
@Log
@Controller

public class UserController {
    @Autowired
    UserService userService;

    //회원가입
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserDto userDto, UserCreateForm userCreateForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
            bindingResult.rejectValue("password2","PasswordInCorrect",
                    "패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        userService.userJoin(userDto);
        log.info("회원가입 성");

        return "redirect:/login";
    }

    @GetMapping("/signin")
    public User signIn(@RequestBody User requestbody) {
        String email = requestbody.getEmail();
        String password = requestbody.getPassword();
        User user = User.builder().build();
        return user;
    }


    @PostMapping("/delete")
    public void delete(@RequestBody User requestbody) {
        Long deleteId = requestbody.getId();
        userService.deleteById(deleteId);
    }

    @PostMapping("/update")
    public void update(@RequestBody User requestbody) {
        Long newId = requestbody.getId();
        userService.update(newId);
    }

    @PostMapping("/read")
    public void getAllUser(@RequestBody User requestbody) {
        userService.findAll();
    }
}
