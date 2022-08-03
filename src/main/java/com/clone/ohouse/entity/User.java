package com.clone.ohouse.entity;

import lombok.*;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    private String password;
    @Column(nullable = false, length =10)
    private String nickname;
    @Column(nullable = false, length = 11)
    private Integer phone;
    @Column(nullable = false,length = 10)
    private String birthday;

    @Column(length = 1000)
    private String refreshToken;
    @Builder
    public User(String email,String password,String nickname, int phone, String birthday){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;
        this.refreshToken = refreshToken;
    }
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateUser(String email,String password){
        this.email = email;
        this.password = password;
    }
//    public void encodePassword(PasswordEncoder passwordEncoder){
//        this.password = passwordEncoder.encode(password);
//    }
    public void destroyToken(){
        this.refreshToken = null;
    }
}
