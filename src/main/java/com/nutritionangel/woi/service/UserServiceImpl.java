package com.nutritionangel.woi.service;

import com.nutritionangel.woi.dto.user.LoginRequestDTO;
import com.nutritionangel.woi.dto.user.UserRegisterDTO;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Integer register(UserRegisterDTO requestDto) throws Exception {
        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getCheckedPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        UserEntity user = requestDto.toEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);

        return user.getUserId();
    }

    @Transactional
    @Override
    public UserEntity getLoginUserByLoginId(String loginId) {
        if (loginId == null) return null;
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);
        if (optionalUser.isEmpty()) return null;
        return optionalUser.get();
    }

    @Transactional
    @Override
    public UserEntity login(LoginRequestDTO loginRequestDto) {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginRequestDto.getLoginId());

        if (optionalUser.isEmpty()) {
            return null;
        }

        UserEntity user = optionalUser.get();

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    @Transactional
    @Override
    public UserEntity getUserByUserId(int userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.get();
    }

    @Transactional
    @Override
    public UserEntity findUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public void logout(String loginId) {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);
        optionalUser.ifPresent(userRepository::delete);
    }
}
