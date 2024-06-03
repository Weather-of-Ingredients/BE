package com.nutritionangel.woi.dto.user;

import com.nutritionangel.woi.entity.UserEntity;
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

//    @Builder
//    public UserEntity toEntity(){
//        UserRole role = (this.role != null) ? this.role : UserRole.USER;
//        return UserEntity.builder()
//                .loginId(loginId)
//                .password(password)
//                .name(name)
//                .email(email)
//                .phoneNum(phoneNum)
//                .school(school)
//                .role(role)
//                .build();
//    }
//@Builder
//public UserRegisterDTO(String name, String loginId, String password, String checkedPassword, String email, String phoneNum, String school, UserRole role) {
//    this.name = name;
//    this.loginId = loginId;
//    this.password = password;
//    this.checkedPassword = checkedPassword;
//    this.email = email;
//    this.phoneNum = phoneNum;
//    this.school = school;
//    this.role = role != null ? role : UserRole.USER; // 기본값을 USER로 설정
//}

    public UserEntity toEntity() {
        UserRole role = (this.role != null) ? this.role : UserRole.USER;
        return UserEntity.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .school(school)
                .role(role)
                .build();
    }


}