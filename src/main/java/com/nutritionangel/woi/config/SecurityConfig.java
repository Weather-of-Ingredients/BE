package com.nutritionangel.woi.config;

import com.nutritionangel.woi.enums.UserRole;
import com.nutritionangel.woi.jwt.JwtFilter;
import com.nutritionangel.woi.jwt.JwtUtil;
import com.nutritionangel.woi.repository.UserRepository;
import com.nutritionangel.woi.service.OAuthLoginService;
import com.nutritionangel.woi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    private final OAuthLoginService oAuthLoginService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(((auth) -> auth.disable()))
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .cors((auth) -> auth.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**", "/api/user/login/**", "/api/user/register/**", "/login/oauth2/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole(UserRole.ADMIN.name())
                                .requestMatchers("api/getNut/**").permitAll() // 임시 코드 (인증 문제 해결 후 지울 것)
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuthLoginService))
                )
                .addFilterBefore(new JwtFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/home", true)
//                                .failureUrl("/login?error=true")
//                                .userInfoEndpoint(userInfoEndpoint ->
//                                        userInfoEndpoint
//                                                .userService(customOAuth2UserService())
//                                )
//                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

//    @Bean
//    public CustomOAuth2UserService customOAuth2UserService() {
//        return new CustomOAuth2UserService(userRepository);
//    }
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
//    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = List.of(kakaoClientRegistration());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
                .clientId("5fe28bb20d52b2c4d6767d930d1614e6")
                .clientSecret("w92RDG9RzcUxGMq3o2oMJIervDQCRsfS")
                .redirectUri("{baseUrl}/login/oauth2/code/kakao")
                .authorizationGrantType(new AuthorizationGrantType("authorization_code"))
                .scope("profile_nickname")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .clientName("Kakao")
                .build();
    }
}