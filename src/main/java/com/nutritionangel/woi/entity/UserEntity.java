package com.nutritionangel.woi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    private String provider; //google, naver 등
    private String providerId; //소셜 로그인 user의 고유ID

}
