package com.clone.ohouse.community.dto;
import com.clone.ohouse.community.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LoginResponseDTO {
    private String email;
    private String nickName;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;

    private String token; // 인증 토큰

    private String message; // 응답 메시지
    public LoginResponseDTO(Optional<User> user, String token) {
        this.email = user.get().getEmail();
        this.nickName = user.get().getNickname();
        this.joinDate = LocalDate.from(user.get().getRegDate());
        this.token = token;
    }
}
