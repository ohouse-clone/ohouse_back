package com.clone.ohouse.community.controller;

import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Optional;

@Controller

public class UserController {
    @Autowired
    UserService userService;

  @PostMapping("/signin")
    public User signIn(@RequestBody User requestbody){
      String email = requestbody.getEmail();
      String password = requestbody.getPassword();
      User user = User.builder().build();
      return user;
  }
  @PostMapping("/delete")
    public void delete(@RequestBody User requestbody){
      Long deleteId = requestbody.getId();
      userService.deleteById(deleteId);
  }
  @PostMapping("/update")
    public void update(@RequestBody User requestbody){
     userService.deleteById(requestbody.getId());

  }
}
