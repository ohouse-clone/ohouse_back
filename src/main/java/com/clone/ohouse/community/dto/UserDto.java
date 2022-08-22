package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.User;

import lombok.*;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString

public class UserDto {
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String birthday;

    public User toEntity(){
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phone(phone)
                .birthday(birthday)
                .build();
        return user;
    }

    @Builder
    public UserDto(String email, String password, String nickname, String phone, String birthday) {

        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;

    }
}
