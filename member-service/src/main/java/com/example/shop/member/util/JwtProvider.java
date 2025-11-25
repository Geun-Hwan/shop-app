package com.example.shop.member.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    @Value("${token.secretKey}")
    private String TOKEN_SECRET;

        //    1hour
    private static final long JWT_EXPIRATION_MS = 1000 * 60 * 60;

    private  static final  long JWT_REFRESH_TOKEN =  JWT_EXPIRATION_MS * 24 * 7L;

    public String generateToken(Authentication authentication) {


        Date now = new Date();

        Date expireDate = new Date(now.getTime() + JWT_EXPIRATION_MS);
        byte[] keyBytes = TOKEN_SECRET.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .subject(authentication.getName())   // principal을 문자열로 얻는 더 안전한 방식
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(key, Jwts.SIG.HS512)       // 최신 API 방식
            .compact();
    }

    public String generateRefreshToken(Authentication authentication) {


        Date now = new Date();

        Date expireDate = new Date(now.getTime() + JWT_REFRESH_TOKEN);
        byte[] keyBytes = TOKEN_SECRET.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .subject(authentication.getName())   // principal을 문자열로 얻는 더 안전한 방식
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(key, Jwts.SIG.HS512)       // 최신 API 방식
            .compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8))).build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtException("Token expired");
        } catch (JwtException jwtException) {
            throw new JwtException("JWT error");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getUserDataFromJwt(String token) {

        try {

            return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes(StandardCharsets.UTF_8))).build()
                .parseSignedClaims(token).getPayload().getSubject();

        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtException("Token expired");
        } catch (JwtException jwtException) {
            throw new JwtException("JWT error");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
