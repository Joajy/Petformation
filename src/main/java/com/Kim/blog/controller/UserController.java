package com.Kim.blog.controller;

import com.Kim.blog.model.KakaoProfile;
import com.Kim.blog.model.OAuthToken;
import com.Kim.blog.model.RoleType;
import com.Kim.blog.model.User;
import com.Kim.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

/*
    인증이 필요한 사용자에게 /auth/** 출입 경로 허용
 */
@Controller
public class UserController {

    //Absolutely important Main key(Will never leaked!)
    @Value("${BlueStar.key}")
    private String blueStarKey;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        //Make new HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Accept", "application/json");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Make new HttpBody Object
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "d8b0fe0ec31fa20c787dcf4706b50843");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        //HttpHeader & HttpBody를 하나의 Object에 보관
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest
                = new HttpEntity<>(params, headers);

        //Http Request by POST
        //Receive Response
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
//        String Request_URL = "https://kauth.kakao.com/oauth/token";
//        ResponseEntity<String> response =
//                restTemplate.postForEntity(Request_URL, kakaoTokenRequest, String.class);

        //Save Token Info to Object
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch(JsonMappingException e){
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("Kakao Access Token: "+ oAuthToken.getAccess_token());

        //Access Token for request user profile
        RestTemplate profileRestTemplate = new RestTemplate();

        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        profileHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest
                = new HttpEntity<>(profileHeaders);

        ResponseEntity<String> profileResponse = profileRestTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper profileObjectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = profileObjectMapper.readValue(profileResponse.getBody(), KakaoProfile.class);
        } catch(JsonMappingException e){
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(blueStarKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .role(RoleType.USER)
                .oauth("kakao")
                .build();

        //check if that id was already joined
        User originUser = userService.findUser(kakaoUser.getUsername());

        if(originUser.getUsername() == null) {
            userService.join(kakaoUser);
        }

        //processing Login
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), blueStarKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
