package com.Kim.blog.model;

import lombok.Data;

@Data
public class KakaoProfile {

    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public static class Properties {
        public String nickname;
    }

    @Data
    public static class KakaoAccount {
        public Profile profile;
        public String email;
        public Boolean profile_nickname_needs_agreement;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;

        @Data
        public static class Profile {
            public String nickname;
        }
    }
}