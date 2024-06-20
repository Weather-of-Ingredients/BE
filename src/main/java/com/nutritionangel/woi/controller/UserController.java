package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.code.ResponseCode;
import com.nutritionangel.woi.dto.response.ResponseDTO;
import com.nutritionangel.woi.dto.user.LoginRequestDTO;
import com.nutritionangel.woi.dto.user.UserInfoResponseDTO;
import com.nutritionangel.woi.dto.user.UserLoginDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.jwt.JwtUtil;
import com.nutritionangel.woi.projection.user.GetUser;
import com.nutritionangel.woi.repository.UserRepository;
import com.nutritionangel.woi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserRepository userRepository;

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

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userEntity = userService.getUserByLoginId(userDetails.getUsername());
        if (userEntity == null) {
            return ResponseEntity.status(401).build();
        }
        UserInfoResponseDTO userInfoResponse = new UserInfoResponseDTO();
        userInfoResponse.setUid(userEntity.getUserId());
        userInfoResponse.setName(userEntity.getName());
        userInfoResponse.setEmail(userEntity.getEmail());
        userInfoResponse.setSchool(userEntity.getSchool());

        return ResponseEntity.ok(userInfoResponse);
    }
}