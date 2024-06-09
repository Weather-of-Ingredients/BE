package com.nutritionangel.woi.dto.user;

import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.enums.OAuthProvider;
import com.nutritionangel.woi.enums.UserRole;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTO {

    private String name; // 이름

    private String loginId; // 아이디

    private String password; // 비밀번호

    private String checkedPassword; // 비밀번호 확인

    private String email;

    private String phoneNum;

    private String school;

    private UserRole role;

    private OAuthProvider oAuthProvider;



    public UserEntity toEntity() {
        UserRole role = (this.role != null) ? this.role : UserRole.USER;
        OAuthProvider oAuthProvider = (this.oAuthProvider != null) ? this.oAuthProvider : OAuthProvider.LOCAL;
        return UserEntity.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .school(school)
                .role(role)
                .oAuthProvider(oAuthProvider)
                .build();
    }


}