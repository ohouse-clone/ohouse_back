package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.User;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString

public class UserDto {
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
    public UserDto(Long id, String email, String password, String nickname, String phone, String birthday, Integer point) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;
        this.point = point;
    }
}
