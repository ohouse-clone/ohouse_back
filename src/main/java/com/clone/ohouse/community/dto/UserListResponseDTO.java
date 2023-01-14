package com.clone.ohouse.community.dto;
import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class UserListResponseDTO {

    private Integer count;
    private List<UserResponseDTO> users;
}
