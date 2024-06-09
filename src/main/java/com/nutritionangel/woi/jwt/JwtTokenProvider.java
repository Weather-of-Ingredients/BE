package com.nutritionangel.woi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;




@Component
public class JwtTokenProvider {

//    private final Key key;
//
//    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }

    private static SecretKey cachedSecretKey;

    private final String secretKeyPlain;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKeyPlain) {
        this.secretKeyPlain = secretKeyPlain;
    }

    private SecretKey _getSecretKey() {
//        log.info(secretKeyPlain);
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();
        return cachedSecretKey;
    }

    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

//@Component
//public class JwtTokenProvider {
//
//    private final Key key;
//
//    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generate(String subject, Date expiredAt) {
//        return Jwts.builder()
//                .setSubject(subject)
//                .setExpiration(expiredAt)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public String extractSubject(String accessToken) {
//        Claims claims = parseClaims(accessToken);
//        return claims.getSubject();
//    }
//
//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parser()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
//}