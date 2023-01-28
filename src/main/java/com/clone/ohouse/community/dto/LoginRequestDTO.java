package com.clone.ohouse.community.dto;
import lombok.*;
@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;
    private String nickname;
}
