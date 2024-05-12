package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.projection.user.GetUser;
import com.nutritionangel.woi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public GetUser register(UserRegisterDTO userRegisterDTO) {
        UserEntity user = UserEntity.builder()
                .loginId(userRegisterDTO.getLoginId())
                .password(userRegisterDTO.getPassword())
                .name(userRegisterDTO.getName())
                .email(userRegisterDTO.getEmail())
                .phoneNum(userRegisterDTO.getPhoneNum())
                .school(userRegisterDTO.getSchool())
                .build();

        UserEntity createUser = userRepository.save(user);
        return EntityToProjectionUser(createUser);
    }

    private GetUser EntityToProjectionUser(UserEntity user){
        GetUser userInfo = new GetUser() {
            @Override
            public String getLoginId() {
                return user.getLoginId();
            }

            @Override
            public String getName() {
                return user.getName();
            }


            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @Override
            public String getPhoneNum() {
                return user.getPhoneNum();
            }

            @Override
            public String getSchool() {
                return user.getSchool();
            }
        };

        return userInfo;
    }

}