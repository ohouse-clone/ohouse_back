package com.clone.ohouse.community.service;

import com.clone.ohouse.community.dto.UserModifyDTO;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional

public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(s->users.add(s));
        return users;
    }

    public Optional<User> findByEmail(String email){
        Optional<User> user =  userRepository.findByEmail(email);
        return user;
    }
    public User findById(Long id){
        User user = userRepository.findOne(id);
        return user;
    }

    public boolean delete(Long id){
        return userRepository.delete(id);
    }

    public boolean save(User user){

        return userRepository.store(user);
    }

    public boolean update(UserModifyDTO userModifyDTO, Long id) {
        User user = userRepository.findOne(id);
                user.setEmail(userModifyDTO.getEmail());
                user.setPassword(userModifyDTO.getPassword());
                user.setNickname(userModifyDTO.getNickname());
                user.setPhone(userModifyDTO.getPhone());

        return userRepository.store(user);
    }

}
