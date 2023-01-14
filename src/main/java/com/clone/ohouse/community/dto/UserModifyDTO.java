package com.clone.ohouse.community.dto;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserModifyDTO {
    private String email;
    private String password;
    private String nickname;
    private String phone;
}
