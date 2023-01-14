package com.clone.ohouse.community.repository;

import com.clone.ohouse.community.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepositoryImpl

{
    private static final Map<Long,User> users = new HashMap<>();

    public List<User> findAll(){
        return users.keySet().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public User findOne(Long id){
        return users.get(id);
    }

    public boolean store(User user){
        User newUser= users.put(user.getId(),user);
        return true;
    }

    public boolean delete(Long id){
        User remove = users.remove(id);
        return remove != null;
    }
}
