package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.code.ResponseCode;
import com.nutritionangel.woi.dto.response.ResponseDTO;
import com.nutritionangel.woi.dto.user.LoginRequestDTO;
import com.nutritionangel.woi.dto.user.UserLoginDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.jwt.JwtUtil;
import com.nutritionangel.woi.projection.user.GetUser;
import com.nutritionangel.woi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(@RequestParam("loginId") String loginId) {
        userService.logout(loginId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_LOGOUT.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_LOGOUT, null));
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String adminPage() {
        return "관리자";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("name", principal.getName());
        return "home";
    }
}