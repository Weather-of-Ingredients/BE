//package com.nutritionangel.woi.domain;
//
//import com.nutritionangel.woi.enums.OAuthProvider;
//import lombok.RequiredArgsConstructor;
//
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.beans.factory.annotation.Value;
//@Component
//@RequiredArgsConstructor
//public class KakaoApiClient implements OAuthApiClient {
//
//
//    private static final String GRANT_TYPE = "authorization_code";
//
//    @Value("${oauth.kakao.url.auth}")
//    private String authUrl;
//
//    @Value("${oauth.kakao.url.api}")
//    private String apiUrl;
//
//    @Value("${oauth.kakao.client-id}")
//    private String clientId;
//
//    private final RestTemplate restTemplate;
//
//    @Override
//    public OAuthProvider oAuthProvider() {
//        return OAuthProvider.KAKAO;
//    }
//
//    @Override
//    public String requestAccessToken(OAuthLoginParams params) {
//        String url = authUrl + "/oauth/token";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = params.makeBody();
//        body.add("grant_type", GRANT_TYPE);
//        body.add("client_id", clientId);
//
//        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
//
//        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);
//
//        assert response != null;
//        return response.getAccessToken();
//    }
//
//    @Override
//    public OAuthInfoResponse requestOauthInfo(String accessToken) {
//        String url = apiUrl + "/v2/user/me";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        httpHeaders.set("Authorization", "Bearer " + accessToken);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("property_keys", "[\"kakao_account.profile\"]");
//
//        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
//
//        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
//    }
//}

package com.nutritionangel.woi.domain;

import com.nutritionangel.woi.enums.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private static final Logger logger = LoggerFactory.getLogger(KakaoApiClient.class);
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

//    @Value("${oauth.kakao.client-secret}")
//    private String clientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

//    @Override
//    public String requestAccessToken(OAuthLoginParams params) {
//        String url = authUrl + "/oauth/token";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", GRANT_TYPE);
//        body.add("redirect_uri", redirectUri);
//        body.add("client_id", clientId);
////        body.add("client_secret", clientSecret);
//
//        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
//
//        logger.debug("Requesting access token from Kakao with URL: {}", url);
//        logger.debug("Request headers: {}", httpHeaders);
//        logger.debug("Request body: {}", body);
//
//        KakaoTokens response = null;
//        try {
//            response = restTemplate.postForObject(url, request, KakaoTokens.class);
//        } catch (Exception e) {
//            logger.error("Error requesting access token from Kakao", e);
//            throw e;
//        }
//
//        if (response == null) {
//            logger.error("Received null response from Kakao when requesting access token");
//            throw new RuntimeException("Failed to retrieve access token from Kakao");
//        }
//
//        return response.getAccessToken();
//    }
    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);

        assert response != null;
        return response.getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        // 추가 로그
        logger.debug("Requesting user info from Kakao with URL: {}", url);
        logger.debug("Request headers: {}", httpHeaders);

        OAuthInfoResponse response = null;
        try {
            response = restTemplate.postForObject(url, request, KakaoInfoResponse.class);
        } catch (Exception e) {
            logger.error("Error requesting user info from Kakao", e);
            throw e; // 필요에 따라 적절한 예외 처리
        }

        if (response == null) {
            logger.error("Received null response from Kakao when requesting user info");
            throw new RuntimeException("Failed to retrieve user info from Kakao");
        }

        return response;
    }
}
