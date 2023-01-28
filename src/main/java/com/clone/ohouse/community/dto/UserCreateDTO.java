package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.User;

import lombok.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.criteria.CriteriaBuilder;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Log
@Slf4j
@Builder
public class UserCreateDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String birthday;
    private Integer point;

    public User toEntity(){
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .phone(phone)
                .birthday(birthday)
                .point(point)
                .build();
        return user;
    }

}
