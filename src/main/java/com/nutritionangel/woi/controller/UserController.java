package com.nutritionangel.woi.controller;

import com.nutritionangel.woi.code.ResponseCode;
import com.nutritionangel.woi.dto.response.ResponseDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.projection.user.GetUser;
import com.nutritionangel.woi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody UserRegisterDTO userRegisterDTO) {
        GetUser res = userService.register(userRegisterDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_REGISTER.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_REGISTER, res));
    }
}