package com.Kim.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/joinForm")
    public String join() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String login(){
        return "user/loginForm";
    }
}
