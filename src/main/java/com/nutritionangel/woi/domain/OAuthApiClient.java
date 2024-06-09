package com.nutritionangel.woi.domain;

import com.nutritionangel.woi.enums.OAuthProvider;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}