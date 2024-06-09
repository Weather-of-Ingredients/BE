package com.nutritionangel.woi.domain;

import com.nutritionangel.woi.enums.OAuthProvider;

public interface OAuthInfoResponse {
    String getNickname();
    OAuthProvider getOAuthProvider();
}