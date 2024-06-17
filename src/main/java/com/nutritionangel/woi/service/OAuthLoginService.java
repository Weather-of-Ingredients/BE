//package com.nutritionangel.woi.service;
//
//import com.nutritionangel.woi.auth.AuthTokens;
//import com.nutritionangel.woi.auth.AuthTokensGenerator;
//import com.nutritionangel.woi.domain.OAuthInfoResponse;
//import com.nutritionangel.woi.domain.OAuthLoginParams;
//import com.nutritionangel.woi.domain.RequestOAuthInfoService;
//import com.nutritionangel.woi.entity.UserEntity;
//import com.nutritionangel.woi.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import static com.amazonaws.services.ec2.model.PrincipalType.User;
//
//@Service
//@RequiredArgsConstructor
//public class OAuthLoginService {
//    private final UserRepository userRepository;
//    private final AuthTokensGenerator authTokensGenerator;
//    private final RequestOAuthInfoService requestOAuthInfoService;
//
//    public AuthTokens login(OAuthLoginParams params) {
//        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
//        Integer userId = findOrCreateUser(oAuthInfoResponse);
//        return authTokensGenerator.generate(userId);
//    }
//
//    private Integer findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
//        return userRepository.findByName(oAuthInfoResponse.getNickname())
//                .map(UserEntity::getUserId)
//                .orElseGet(() -> newUser(oAuthInfoResponse));
//    }
//
//    private Integer newUser(OAuthInfoResponse oAuthInfoResponse) {
//        UserEntity user = UserEntity.builder()
//                .name(oAuthInfoResponse.getNickname())
//                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
//                .build();
//
//        return userRepository.save(user).getUserId();
//    }
//
//
//}

package com.nutritionangel.woi.service;

import com.nutritionangel.woi.auth.AuthTokens;
import com.nutritionangel.woi.auth.AuthTokensGenerator;
import com.nutritionangel.woi.domain.OAuthInfoResponse;
import com.nutritionangel.woi.domain.OAuthLoginParams;
import com.nutritionangel.woi.domain.RequestOAuthInfoService;
import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuthLoginService extends DefaultOAuth2UserService {
    private static final Logger log = LoggerFactory.getLogger(OAuthLoginService.class);
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Integer userId = findOrCreateUser(oAuthInfoResponse);
        return authTokensGenerator.generate(userId);
    }

    private Integer findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByName(oAuthInfoResponse.getNickname())
                .map(UserEntity::getUserId)
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private Integer newUser(OAuthInfoResponse oAuthInfoResponse) {
        UserEntity user = UserEntity.builder()
                .name(oAuthInfoResponse.getNickname())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return userRepository.save(user).getUserId();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("OAuth2 User Request: {}", userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2 User: {}", oAuth2User);

        // 여기에 추가로 사용자 정보를 처리하는 로직을 작성할 수 있습니다.
        // 예를 들어, 사용자 정보를 데이터베이스에 저장하거나 추가 정보를 추출할 수 있습니다.

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "name"); // 여기에 기본 사용자 이름 속성 키를 넣습니다.
    }
}
