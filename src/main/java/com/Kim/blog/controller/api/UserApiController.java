package com.Kim.blog.controller.api;

import com.Kim.blog.dto.ResponseDto;
import com.Kim.blog.dto.UserRequestDto;
import com.Kim.blog.service.UserService;
import com.Kim.blog.validator.CheckEmailValidator;
import com.Kim.blog.validator.CheckNicknameValidator;
import com.Kim.blog.validator.CheckUsernameValidator;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
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

//    @PostMapping("/auth/joinProc")
//    public ResponseDto<Integer> save(@RequestBody User user) {
//        System.out.println("UserApiController: save 함수 호출");
//        user.setRole(RoleType.USER);
//        userService.join(user);
//        return new ResponseDto<>(HttpStatus.OK.value(), 1);
//    }

    @PostMapping("/auth/joinProc")
    public ResponseDto<?> save(@Valid @RequestBody UserRequestDto userDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(bindingResult);
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validatorResult);
        }
        userService.save(userDto);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

//    @PutMapping("/user")
//    public ResponseDto<Integer> update(@RequestBody User user) {    //RequestBody 통해 JSON 형식 데이터를 받아옴
//        userService.update(user);
//        //Transaction 종료로 DB값은 변경되지만, 세션값 변경되지 않으므로 데이터 일치상태 확인 어려움
//
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseDto<>(HttpStatus.OK.value(), 1);
//    }

    @PutMapping("/user")
    public ResponseDto<?> update(@RequestBody UserRequestDto userDto) {
        userService.update(userDto);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/user/{user_id}/profileImageUrl")
    public ResponseDto<?> profileImageUpdate(@PathVariable Long user_id, MultipartFile profileImageFile) {
        userService.profileImageUpdate(user_id, profileImageFile);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
