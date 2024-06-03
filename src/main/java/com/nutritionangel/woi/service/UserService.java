package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.user.LoginRequestDTO;
import com.nutritionangel.woi.dto.user.UserLoginDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.projection.user.GetUser;

public interface UserService {


    public Integer register(UserRegisterDTO requestDto) throws Exception;

    public UserEntity getLoginUserByLoginId(String loginId);

    public UserEntity login(LoginRequestDTO loginRequestDto);

    public UserEntity getUserByUserId(int userId);
    public UserEntity findUserById(int userId);
    public UserEntity saveUser(UserEntity user);
//    public UserEntity getCurrentUser();

    public void logout(String loginId);

}


