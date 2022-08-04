package com.clone.ohouse.repository;


import com.clone.ohouse.dto.UserDto;
import com.clone.ohouse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<UserDto,String> {
    Optional<User> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}
