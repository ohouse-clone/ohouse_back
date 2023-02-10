package com.clone.ohouse.community.entity;

import lombok.Builder;

import com.clone.ohouse.community.domain.cardcollections.Card;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(nullable = false, length = 100, unique = true)
    private String password;
    @Column(nullable = false, length = 30)
    private String nickname;
    @Column(nullable = false, length = 50)
    private String phone;
    @Column
    private String birthday;
    @Column(length = 1000)
    private String refreshToken;

    @Column(nullable = false ,length = 32)
    private String name;

    @Column(length = 512)
    private String picture;

    @Column
    @CreationTimestamp
    private LocalDateTime regDate;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void destroyToken() {
        this.refreshToken = null;
    }
    @Builder
    public User(Long id,String email,String password,String nickname,String phone,LocalDateTime regDate, String name,String picture){

        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.regDate = regDate;
        this.name = name;
        this.picture = picture;
    }
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }


}
