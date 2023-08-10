package com.Kim.community.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class EncTest {

    @Test
    public void encryptHash(){
        String encPassword = new BCryptPasswordEncoder().encode("10000");
        System.out.println("10000 해쉬값 = " + encPassword);
    }
}
