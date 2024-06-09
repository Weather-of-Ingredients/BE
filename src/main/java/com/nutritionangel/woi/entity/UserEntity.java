package com.nutritionangel.woi.entity;

import com.nutritionangel.woi.enums.OAuthProvider;
import com.nutritionangel.woi.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(unique = true, nullable = false, length = 45)
    private String loginId;

    @Column(length=100, nullable = false)
    private String password;

    @Column(length=100, nullable = false)
    private String name;

    @Column(length=100, nullable = false)
    private String email;

    @Column(length=100, nullable = false)
    private String phoneNum;

    @Column(length=100, nullable = false)
    private String school;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider oAuthProvider = OAuthProvider.LOCAL;
    //private String providerId; // 소셜 로그인 user의 고유ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public Integer getUserId() {
        return userId;
//    }

//    @Builder
//    public UserEntity(String loginId, String password, String name, String email, String phoneNum, String school, UserRole role, OAuthProvider oAuthProvider) {
//        this.loginId = loginId;
//        this.password = password;
//        this.name = name;
//        this.email = email;
//        this.phoneNum = phoneNum;
//        this.school = school;
//        this.role = role;
//        this.oAuthProvider = oAuthProvider;
    }

//    public UserEntity update(String name) {
//        this.name = name;
//        return this;
//    }


}
