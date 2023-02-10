package com.clone.ohouse.auth;

import com.clone.ohouse.community.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter

public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String nickname;
    private String email;
    private User user;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofNaver(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        log.info("응답" + response);

        return OAuthAttributes.builder()
                .username((String) response.get("username"))
                .email((String) response.get("email"))
                .nickname((String) response.get("nickname"))
//                .attributes(response)
//                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    @Builder
    public OAuthAttributes(String username, String nickname, String email) {

        this.username = username;
        this.email = email;
        this.nickname = nickname;

    }

    //ROLE 설정은 나중에
    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .build();
    }
}
