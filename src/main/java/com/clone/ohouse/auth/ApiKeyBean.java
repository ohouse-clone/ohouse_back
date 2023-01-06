package com.clone.ohouse.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ApiKeyBean {

    private String api;
    private String secret;
    private String callback;
}
