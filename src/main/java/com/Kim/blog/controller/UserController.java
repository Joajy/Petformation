package com.Kim.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
    인증이 필요한 사용자에게 /auth/** 출입 경로 허용
 */
@Controller
public class UserController {

    @GetMapping("/auth/joinForm")
    public String join() {
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String login(){
        return "user/loginForm";
    }
}
