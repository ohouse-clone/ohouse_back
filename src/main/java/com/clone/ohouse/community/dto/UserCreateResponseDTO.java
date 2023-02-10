package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserCreateResponseDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;

    public UserCreateResponseDTO(Optional<User> user){
        this.id = user.get().getId();
        this.email = user.get().getEmail();
        this.password = user.get().getPassword();
        this.nickname = user.get().getNickname();
        this.regDate = user.get().getRegDate();

    }

    public UserCreateResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.regDate = user.getRegDate();
    }
}
