package com.example.shop.member.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    private static final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7L;

    public String generateToken(Authentication authentication) {

        Date now = new Date();

        Date expireDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder().subject((String) authentication.getPrincipal()).issuedAt(now)
            .expiration(expireDate).signWith(
                Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes()), Jwts.SIG.HS512).compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes())).build()
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
            return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes())).build()
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
