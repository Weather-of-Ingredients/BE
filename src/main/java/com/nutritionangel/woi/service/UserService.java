package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.projection.user.GetUser;

public interface UserService {

    GetUser register(UserRegisterDTO userRegisterDTO);

}


