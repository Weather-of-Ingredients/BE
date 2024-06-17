package com.nutritionangel.woi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class PasswordEncoderConfig {
    // jwt
    @Bean
    public PasswordEncoder passwordEncoder(){
        // 개발 환경에서 임시로 사용 -> 나중에 return new BCryptPasswordEncoder(); 이렇게 바꿔야 함.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
