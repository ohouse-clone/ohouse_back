package com.clone.ohouse.community.service;

import com.clone.ohouse.community.dto.LoginResponseDTO;
import com.clone.ohouse.community.dto.UserCreateDTO;
import com.clone.ohouse.community.dto.UserCreateResponseDTO;
import com.clone.ohouse.community.dto.UserModifyDTO;
import com.clone.ohouse.community.entity.User;
import com.clone.ohouse.community.repository.UserRepository;
import com.clone.ohouse.exception.DuplicatedEmailException;
import com.clone.ohouse.exception.NoRegisteredArgumentsException;
import com.clone.ohouse.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j


public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

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


    // 회원가입 처리
    public UserCreateResponseDTO create(final UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            throw new NoRegisteredArgumentsException("가입정보가 없습니다.");
        }
        final String email = userCreateDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists - {}", email);
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        // 패스워드 인코딩
        String rawPassword = userCreateDTO.getPassword(); // 평문 암호
        String encodedPassword = passwordEncoder.encode(rawPassword); // 암호화처리
        userCreateDTO.setPassword(encodedPassword);

        User savedUser = userRepository.save(userCreateDTO.toEntity());

        log.info("회원 가입 성공!! - user_id : {}", savedUser.getId());

        return new UserCreateResponseDTO(savedUser);
    }
    // 이메일 중복확인
    public boolean isDuplicate(String email) {
        if (email == null) {
            throw new RuntimeException("이메일 값이 없습니다.");
        }
        return userRepository.existsByEmail(email);
    }

    // 로그인 검증
    public LoginResponseDTO getByCredentials(final String email, final String rawPassword){
        //이메일을 통회 회원정보 조회
        Optional<User> originalUser = userRepository.findByEmail(email);
        if(originalUser == null){
            throw new RuntimeException("가입된 회원이 아닙니다.");
        }
        if(!passwordEncoder.matches(rawPassword, originalUser.get().getPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        log.info("{}님 로그인 성공!",originalUser.get().getNickname());
        // 토큰 발급
        String token = tokenProvider.createToken(originalUser);

        return new LoginResponseDTO(originalUser, token);
    }
}
