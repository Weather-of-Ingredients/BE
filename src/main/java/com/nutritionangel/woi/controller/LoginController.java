package com.nutritionangel.woi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/some-endpoint")
    public String someEndpoint() {
        if (isLoggedIn()) {
            // 로그인 상태일 때 처리할 내용
            return "Logged in!";
        } else {
            // 로그인하지 않은 상태일 때 처리할 내용
            return "Not logged in!";
        }
    }

    private boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}