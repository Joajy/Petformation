package com.Kim.community.model;

import lombok.Data;

@Data
public class OAuthToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private int expiresIn;
    private String scope;
    private String idToken;
    private int refreshTokenExpiresIn;
}
