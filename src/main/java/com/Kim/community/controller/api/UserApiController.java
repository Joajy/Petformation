package com.Kim.community.controller.api;

import com.Kim.community.dto.ResponseDto;
import com.Kim.community.dto.SendTempPwdDto;
import com.Kim.community.dto.UserRequestDto;
import com.Kim.community.repository.UserRepository;
import com.Kim.community.service.UserService;
import com.Kim.community.validator.CheckEmailValidator;
import com.Kim.community.validator.CheckNicknameValidator;
import com.Kim.community.validator.CheckUsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailValidator checkEmailValidator;

    //요청이 들어오기 전에 미리 선언된 메서드 실행
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkEmailValidator);
    }

    @PostMapping("/auth/joinProc")
    public ResponseDto<?> save(@Valid @RequestBody UserRequestDto userDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(bindingResult);
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validatorResult);
        }
        userService.join(userDto);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/user")
    public ResponseDto<?> update(@RequestBody UserRequestDto userDto) {
        userService.update(userDto);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/user/{user_id}/profileImageUrl")
    public ResponseDto<?> profileImageUpdate(@PathVariable("user_id") Long userId, MultipartFile profileImageFile) {
        userService.profileImageUpdate(userId, profileImageFile);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/user/delete/{user_id}")
    public ResponseDto<?> delete(@PathVariable("user_id") Long userId){
        userService.delete(userId);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/auth/find")
    public ResponseDto<?> find(@RequestBody SendTempPwdDto dto) {
        if (!userRepository.existsByUsername(dto.getUsername()) || !Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", dto.getEmail())) {
            Map<String, String> validResult = new HashMap<>();
            if (!userRepository.existsByUsername(dto.getUsername())) {
                validResult.put("valid_username", "존재하지 않는 사용자 이름입니다.");
            }
            if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", dto.getEmail())) {
                validResult.put("valid_email", "올바르지 않은 이메일 형식입니다.");
            }
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validResult);
        }
        userService.sendTempPwd(dto);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
