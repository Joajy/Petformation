package com.Kim.blog.service;

import com.Kim.blog.dto.UserRequestDto;
import com.Kim.blog.model.RoleType;
import com.Kim.blog.model.User;
import com.Kim.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(User user) {
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }


    //Public API Join
    @Transactional
    public void join(UserRequestDto userDto) {
        User user = User.builder()
                .username((userDto.getUsername()))
                .password(encoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(RoleType.USER)
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findUser(String username) {
        User user = userRepository.findByUsername(username).orElseGet(User::new);
        return user;
    }

    @Transactional
    public void update(User user) {
        User persistence = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("해당하는 회원정보가 없습니다."));

        //Check Validation
        //if oAuth field has no value, can change email & password of that account
        String oAuth = persistence.getOauth();
        if(oAuth == null || oAuth.equals("")) {
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistence.setPassword(encPassword);
            persistence.setEmail(user.getEmail());
        }
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
}
