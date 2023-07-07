package com.Kim.blog.service;

import com.Kim.blog.dto.UserRequestDto;
import com.Kim.blog.model.KakaoProfile;
import com.Kim.blog.model.OAuthToken;
import com.Kim.blog.model.RoleType;
import com.Kim.blog.model.User;
import com.Kim.blog.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    //Absolutely important Main key(Never leaked!)
    @Value("${BlueStar.key}")
    private String blueStarKey;

    @Transactional
    public void save(User user) {
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }


    //Public API Join
    @Transactional
    public void save(UserRequestDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .role(RoleType.USER)
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void update(UserRequestDto userDto) {
        User persistence = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("회원정보를 수정할 수 없습니다."));

        persistence.setPassword(encoder.encode(userDto.getPassword()));
        persistence.setNickname(userDto.getNickname());
    }

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String format = String.format("valid_%s", error.getField());
            validatorResult.put(format, error.getDefaultMessage());
        }
        return validatorResult;
    }

    @Transactional
    public void kakaoLogin(String code){
        RestTemplate restTemplate = new RestTemplate();
        //Make new HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

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
        String Request_URL = "https://kauth.kakao.com/oauth/token";
        ResponseEntity<String> response =
                restTemplate.postForEntity(Request_URL, kakaoTokenRequest, String.class);

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

        //Access Token for request user profile
        RestTemplate profileRestTemplate = new RestTemplate();
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.add("Authorization", "Bearer " + oAuthToken.getAccessToken());
        profileHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest
                = new HttpEntity<>(profileHeaders);

        String profileRequest_URL = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<String> profileResponse =
                profileRestTemplate.postForEntity(profileRequest_URL, kakaoProfileRequest, String.class);

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
                .nickname(kakaoProfile.getProperties().getNickname())
                .oauth("kakao")
                .build();

        //check if that id was already joined
        User originUser = userRepository.findByUsername(kakaoUser.getUsername()).orElseGet(User::new);

        if(originUser.getUsername() == null) save(kakaoUser);

        //processing Login
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), blueStarKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
