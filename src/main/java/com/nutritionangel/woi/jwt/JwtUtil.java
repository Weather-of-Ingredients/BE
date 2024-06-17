package com.nutritionangel.woi.jwt;

import com.nutritionangel.woi.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static SecretKey cachedSecretKey;

    private final String secretKeyPlain;

    public JwtUtil(@Value("${jwt.secret}") String secretKeyPlain) {
        this.secretKeyPlain = secretKeyPlain;
    }

    private SecretKey _getSecretKey() {
        log.info(secretKeyPlain);
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();
        return cachedSecretKey;
    }

    // JWT Token 발급
    public String createJwt(String loginId, UserRole role, long expireTimeMs) {
        // Claim = Jwt Token에 들어갈 정보
        // Claim에 loginId를 넣어 줌으로써 나중에 loginId를 꺼낼 수 있음
        // 버전 업데이트 되면서 claim 따로 생성 X
//        Claims claims = (Claims) Jwts.claims();
//        claims.put("loginId", loginId);
//        claims.put("role", role);

        return Jwts.builder()
                .claim("loginId",loginId)
                .claim("role",role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Claims에서 loginId 꺼내기
    public String getLoginId(String token) {
        return extractClaims(token).get("loginId").toString();
    }

    // 발급된 Token이 만료 시간이 지났는지 체크
    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    // SecretKey를 사용해 Token Parsing
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    public int getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }

        String username = authentication.getName();

        if (!username.matches("\\d+")) {
            throw new RuntimeException("올바르지 않은 사용자 ID 형식입니다.");
        }

        return Integer.parseInt(username);
    }
}
