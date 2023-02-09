package com.clone.ohouse.community.service;


import com.clone.ohouse.community.dto.*;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import com.clone.ohouse.exception.DuplicatedEmailException;
import com.clone.ohouse.exception.NoRegisteredArgumentsException;
import com.clone.ohouse.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public User findByEmail(String email){
        User user =  userRepository.findByEmail(email);
        return user;
    }

    // 이메일 중복확인
    public boolean isDuplicate(String email) {
        if (email == null) {
            throw new RuntimeException("이메일 값이 없습니다.");
        }
        return userRepository.existsByEmail(email);
    }

    // 회원가입 처리
    public UserCreateResponseDTO create(final UserCreateDTO userSignUpDTO) {
        if (userSignUpDTO == null) {
            throw new NoRegisteredArgumentsException("가입정보가 없습니다.");
        }
        final String email = userSignUpDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists - {}", email);
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        // 패스워드 인코딩
        String rawPassword = userSignUpDTO.getPassword(); // 평문 암호
        String encodedPassword = passwordEncoder.encode(rawPassword); // 암호화처리
        userSignUpDTO.setPassword(encodedPassword);

        User savedUser = userRepository.save(userSignUpDTO.toEntity());

        log.info("회원 가입 성공!! - user_id : {}", savedUser.getId());

        return new UserCreateResponseDTO(Optional.of(savedUser));
    }

    // 로그인 검증
    public LoginResponseDTO getByCredentials(
            final String email,
            final String rawPassword) {

        // 입력한 이메일을 통해 회원정보 조회
        User originalUser = userRepository.findByEmail(email);

        if (originalUser == null) {
            throw new RuntimeException("가입된 회원이 아닙니다.");
        }
        // 패스워드 검증 (입력 비번, DB에 저장된 비번)
        if (!passwordEncoder.matches(rawPassword, originalUser.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        log.info("{}님 로그인 성공!", originalUser.getNickname());

        // 토큰 발급
        String token = tokenProvider.createToken(originalUser);

        return new LoginResponseDTO(originalUser, token);
    }

    public UserListResponseDTO findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(s->users.add(s));
        if(users.isEmpty()){
            throw new RuntimeException("조회된 유저가 없습니다.");
        }
        List<UserCreateResponseDTO> dtoList = users.stream()
                .map(UserCreateResponseDTO::new)
                .collect(Collectors.toList());
        return UserListResponseDTO.builder()
                .count(users.size())
                .users(dtoList)
                .build();
    }

    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    public User save(User user) {
        userRepository.save(user);
        return user;
    }

}
