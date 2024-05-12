package com.nutritionangel.woi.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS_REGISTER(HttpStatus.OK, "회원가입을 성공했습니다.");
    private final HttpStatus status;
    private final String message;
}