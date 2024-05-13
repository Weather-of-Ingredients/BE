package com.nutritionangel.woi.dto.user;

import org.jetbrains.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    private String name;


    @NotNull
    private String email;

    @NotNull
    private String phoneNum;

    @NotNull
    private String school;

}