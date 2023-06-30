package com.Kim.blog.model;

import lombok.Data;

@Data
public class OAuthToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private String id_token;
    private int refresh_token_expires_in;
}
