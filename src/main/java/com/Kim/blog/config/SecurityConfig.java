package com.Kim.blog.config;

//import com.Kim.blog.config.auth.PrincipalDetailService;
import com.Kim.blog.config.auth.PrincipalDetailService;
import com.Kim.blog.interceptor.NotificationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

//Bean 등록으로 스프링 컨테이너에서 객체 관리
/*@Configuration //Bean 등록
@EnableWebSecurity  //Add Security Filter
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 url 접근 시 권한 및 인증 확인
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }


    //antMatchers에 있는 주소 외에는 모두 인증이 필요함
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
                .loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/loginProc") //Spring Security에서 이 주소로 오는 Login Request를 가로챈다
                .defaultSuccessUrl("/");
//                .failureUrl("/auth/loginForm");
    }
}*/

//WebSecurityConfigurerAdapter가 deprecated인 상태임을 확인, 이후 코드를 작성해가며 오류 검증 후 문제 없을 시 최종장에서 해당 코드로 수정할 예정
//하단의 코드는 최신 Spring Security 적용 결과
@Configuration //Bean 등록
@EnableWebSecurity  //Add Security Filter
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 url 접근 시 권한 및 인증 확인
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final PrincipalDetailService principalDetailService;
    private final AuthenticationFailureHandler userLoginFailHandler;
    private final NotificationInterceptor notificationInterceptor;

    @Value("${file.path}")
    private String uploadFolder;

    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .rememberMe()
                    .rememberMeParameter("remember")
                    .tokenValiditySeconds(1800) //sec
                    .alwaysRemember(false)
                    .userDetailsService(principalDetailService)
                .and()
                .authorizeRequests()
                    .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**","/sort/**", "/board", "/upload/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/auth/loginForm")
                    .loginProcessingUrl("/auth/loginProc") //Spring Security에서 이 주소로 오는 Login Request를 가로챈다
                    .failureHandler(userLoginFailHandler)
                    .defaultSuccessUrl("/")
                .and()
                    .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notificationInterceptor).excludePathPatterns("/js/**", "/css/**", "/image/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry
                .addResourceHandler("/upload/**")	//jsp 페이지에서 '/upload/**'와 같은 주소 패턴이 요청되면 실행
                .addResourceLocations("file:///" + uploadFolder)
                .setCachePeriod(60 * 10 * 6)	//1시간
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
