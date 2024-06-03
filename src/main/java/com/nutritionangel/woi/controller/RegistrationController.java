//package com.nutritionangel.woi.controller;
//
//import com.nutritionangel.woi.dto.user.LoginRequestDTO;
//import com.nutritionangel.woi.dto.user.UserRegisterDTO;
//import com.nutritionangel.woi.entity.UserEntity;
//import com.nutritionangel.woi.jwt.JwtUtil;
//import com.nutritionangel.woi.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RequestMapping("/api/user")
//@RestController
//@RequiredArgsConstructor
//public class RegistrationController {
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    @PostMapping("/register")   // 약을 여러 개 얻어올 수 있는 버전
//    public String processSignup(@RequestBody @Valid UserRegisterDTO userRegisterDto, BindingResult result) throws Exception {
//
//        System.out.println(userRegisterDto);
//
//        if(result.hasErrors()) return "redirect:/register";
//
//        userService.register(userRegisterDto); // 최종적으로 DB에 저장
//
//        return "redirect:/register-completed";
//    }
//
//    // 로그인
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequestDTO loginRequestDto){
//        UserEntity user = userService.login(loginRequestDto);
//
//        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
//        if(user == null){
//            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
//        }
//
//        // 로그인 성공 => Jwt Token 발급
//        long expiredTimeMs = 1000 * 60 * 60 * 24L;  // 1일, 24시간
//        String jwtToken = jwtUtil.createJwt(user.getLoginId(), user.getRole(), expiredTimeMs);
//
//        return jwtToken;
//    }
//}

//package com.nutritionangel.woi.controller;
//
//import com.nutritionangel.woi.dto.user.LoginRequestDTO;
//import com.nutritionangel.woi.dto.user.UserRegisterDTO;
//import com.nutritionangel.woi.entity.UserEntity;
//import com.nutritionangel.woi.jwt.JwtUtil;
//import com.nutritionangel.woi.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RequestMapping("/api/user")
//@RestController
//@RequiredArgsConstructor
//public class RegistrationController {
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    @PostMapping("/register")   // 약을 여러 개 얻어올 수 있는 버전
//    public String processSignup(@RequestBody @Valid UserRegisterDTO userRegisterDto, BindingResult result) throws Exception {
//
//        System.out.println(userRegisterDto);
//
//        if(result.hasErrors()) return "redirect:/register";
//
//        userService.register(userRegisterDto); // 최종적으로 DB에 저장
//
//        return "redirect:/register-completed";
//    }
//
//    // 로그인
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequestDTO loginRequestDto){
//        UserEntity user = userService.login(loginRequestDto);
//
//        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
//        if(user == null){
//            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
//        }
//
//        // 로그인 성공 => Jwt Token 발급
//        long expiredTimeMs = 1000 * 60 * 60 * 24L;  // 1일, 24시간
//        String jwtToken = jwtUtil.createJwt(user.getLoginId(), user.getRole(), expiredTimeMs);
//
//        return jwtToken;
//    }
//}

package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.dto.user.LoginRequestDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.jwt.JwtUtil;
import com.nutritionangel.woi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")   // 약을 여러 개 얻어올 수 있는 버전
    public String processSignup(@RequestBody @Valid UserRegisterDTO userRegisterDto, BindingResult result) throws Exception {

        System.out.println(userRegisterDto);

        if(result.hasErrors()) return "redirect:/register";

        userService.register(userRegisterDto); // 최종적으로 DB에 저장

        return "redirect:/register-completed";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDto){
        UserEntity user = userService.login(loginRequestDto);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(user == null){
            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 => Jwt Token 발급
        long expiredTimeMs = 1000 * 60 * 60 * 24L;  // 1일, 24시간
        String jwtToken = jwtUtil.createJwt(user.getLoginId(), user.getRole(), expiredTimeMs);

        return jwtToken;
    }
}
