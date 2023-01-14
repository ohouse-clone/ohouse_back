package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserResponseDTO {
    private Long id;
    private String email;
    private String password;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.regDate = user.getRegDate();
    }
}
