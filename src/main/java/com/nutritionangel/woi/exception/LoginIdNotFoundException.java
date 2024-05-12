package com.nutritionangel.woi.exception;

import com.nutritionangel.woi.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginIdNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
}