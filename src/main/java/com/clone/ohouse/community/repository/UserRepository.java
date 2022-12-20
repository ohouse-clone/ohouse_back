package com.clone.ohouse.community.repository;


import com.clone.ohouse.community.dto.UserDto;
import com.clone.ohouse.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface  UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
