package com.nutritionangel.woi.service;

import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        UserEntity user =  userRepository.findByName(name)
                .orElseThrow(()->new UsernameNotFoundException("아이디가 존재하지 않습니다"));
        return new UserDetailsImpl(user);

    }

}
