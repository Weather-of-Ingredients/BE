package com.nutritionangel.woi.entity;

import com.nutritionangel.woi.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private int userId;

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

    private String provider; // google, naver 등
    private String providerId; // 소셜 로그인 user의 고유ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
}
