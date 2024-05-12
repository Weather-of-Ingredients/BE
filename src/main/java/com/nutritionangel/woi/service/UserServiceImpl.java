package com.nutritionangel.woi.service;

import com.nutritionangel.woi.code.ErrorCode;
import com.nutritionangel.woi.dto.user.UserLoginDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.exception.LoginIdNotFoundException;
import com.nutritionangel.woi.exception.LoginPasswordNotMatchException;
import com.nutritionangel.woi.projection.user.GetUser;
import com.nutritionangel.woi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public GetUser login(UserLoginDTO userLoginDTO) {
        Optional<UserEntity> findUser = userRepository.findByLoginId(userLoginDTO.loginId);

        // 아이디 존재하는지 확인
        if(!findUser.isPresent()) throw new LoginIdNotFoundException(ErrorCode.USERID_NOT_FOUND);
            // 비밀번호가 같은지 확인
        else if(!findUser.get().getPassword().equals(userLoginDTO.password)) throw new LoginPasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);


        GetUser user = EntityToProjectionUser(findUser.get());
        return user;
    }

}