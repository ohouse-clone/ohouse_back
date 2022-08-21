package com.clone.ohouse.community.service;

import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(s->users.add(s));
        return users;
    }

    public Optional<User> findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User save(User user){
        userRepository.save(user);
        return user;
    }

    public void update(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(newUser->{
                user.get().setEmail(newUser.getEmail());
                user.get().setPassword(newUser.getPassword());

        });

    }

}
