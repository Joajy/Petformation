package com.Kim.blog.config.auth;

import com.Kim.blog.model.User;
import com.Kim.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    //로그인 요청 가로챌 때 사용자명, 비밀번호 가로챔
    //이 때 비밀번호는 자동처리되므로 사용자명 DB에 있는지 여부 확인 후 return
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(()->{
                    return new UsernameNotFoundException("사용자 " + username + "은(는 존재하지 않습니다.");
                });
        return new PrincipalDetail(principal);  //사용자 정보를 Security Session에 저장
    }
}
