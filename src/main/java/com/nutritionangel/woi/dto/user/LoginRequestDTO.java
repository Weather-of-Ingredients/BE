package com.nutritionangel.woi.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LoginRequestDTO {
    // 로그인 시 쓰이는 Dto
    private String loginId;

    private String password;
}