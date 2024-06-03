package com.nutritionangel.woi.config;

import com.nutritionangel.woi.enums.UserRole;
import com.nutritionangel.woi.jwt.JwtFilter;
import com.nutritionangel.woi.jwt.JwtUtil;
import com.nutritionangel.woi.repository.UserRepository;
import com.nutritionangel.woi.service.CustomOAuth2UserService;
import com.nutritionangel.woi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(((auth) -> auth.disable()))
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .cors((auth) -> auth.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/", "/api/user/login/**", "/api/user/register/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole(UserRole.ADMIN.name())
                                .anyRequest().authenticated()
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
}
