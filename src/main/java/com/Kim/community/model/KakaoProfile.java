package com.Kim.community.model;

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
        public Boolean profileNicknameNeedsAgreement;
        public Boolean hasEmail;
        public Boolean emailNeedsAgreement;
        public Boolean isEmailValid;
        public Boolean isEmailVerified;

        @Data
        public static class Profile {
            public String nickname;
        }
    }
}