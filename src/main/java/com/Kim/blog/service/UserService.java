package com.Kim.blog.service;

import com.Kim.blog.dto.SendTempPwdDto;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;

    //Absolutely important Main key(Never leaked!)
    @Value("${BlueStar.key}")
    private String blueStarKey;

    @Value("${file.path}")
    private String uploadFolder;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Transactional
    public void join(User user) {
        userRepository.save(user);
    }

    //Public API Join
    @Transactional
    public void join(UserRequestDto userDto) {
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

    @Transactional
    public void delete(Long userId){
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
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

        if(originUser.getUsername() == null) join(kakaoUser);

        //processing Login
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), blueStarKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    public void profileImageUpdate(Long userId, MultipartFile profileImageFile) {
        UUID uuid = UUID.randomUUID();
        String imgFileName = uuid + "_" + profileImageFile.getOriginalFilename();
        Path imgFilePath = Paths.get(uploadFolder + imgFileName);

        try {
            Files.write(imgFilePath, profileImageFile.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("프로필 이미지 수정 실패: 존재하지 않는 회원입니다."));

        user.setProfileImageUrl(imgFileName);
    }

    @Transactional
    public void sendTempPwd(SendTempPwdDto dto) {

        char[] set = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};
        String tempPassword = "";
        for (int i = 0; i < 12; i++) {
            tempPassword += set[(int)(set.length * Math.random())];
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(dto.getEmail());
            message.setFrom(sendFrom);
            message.setSubject("임시 비밀번호 안내 이메일입니다.");
            message.setText("안녕하세요.\n"
                    + "임시비밀번호 안내 관련 이메일 입니다.\n"
                    + "임시 비밀번호를 발급하오니 홈페이지에 접속하셔서 로그인 하신 후, \n"
                    + "반드시 비밀번호를 변경해주시기 바랍니다.\n"
                    + "임시 비밀번호 : " + tempPassword);
            javaMailSender.send(message);
        } catch (MailParseException e) {
            e.printStackTrace();
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
        } catch (MailSendException e) {
            e.printStackTrace();
        } catch (MailException e) {
            e.printStackTrace();
        }
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new IllegalArgumentException("임시 비밀번호 변경 실패: 사용자 이름을 찾을 수 없습니다."));
        user.setPassword(encoder.encode(tempPassword));
    }
}
