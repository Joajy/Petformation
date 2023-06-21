package com.Kim.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//Bean 등록으로 스프링 컨테이너에서 객체 관리
@Configuration //Bean 등록
@EnableWebSecurity  //Add Security Filter
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 url 접근 시 권한 및 인증 확인
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/auth/loginForm");
    }
}

//WebSecurityConfigurerAdapter가 deprecated되었음을 확인, 이후 코드를 작성해가며
/*
@Configuration //Bean 등록
@EnableWebSecurity  //Add Security Filter
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 url 접근 시 권한 및 인증 확인
@RequiredArgsConstructor
public class SecurityConfig {

    public BCryptPasswordEncoder encodePWD(){
        String encPassword = new BCryptPasswordEncoder().encode("1234");


        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/loginForm")
                .and().build();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//            .and()
//                .formLogin()
//                .loginPage("/auth/loginForm");
//    }
}
*/