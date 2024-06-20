package com.nutritionangel.woi.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponseDTO {

    private Integer uid;

    private String name;
    private String email;

    private String school;
}


