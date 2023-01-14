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
//암호화
    public void encryptPassword(String BCryptpassword){
        this.password = BCryptpassword;
    }

    @Builder
    public UserCreateDTO(Long id, String email, String password, String nickname, String phone, String birthday, Integer point) {

        this.id = id;
        this.email = email;
        this.password = (new BCryptPasswordEncoder().encode(password));
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;
        this.point = point;
    }
}
