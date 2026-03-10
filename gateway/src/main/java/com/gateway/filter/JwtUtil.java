package com.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private static final String SECRET =
            "9f4c2e7b3d8a1f6c5b9e0a2d4c7f8b1e6d3a9c2e5f7b4a1d8c6e3f0b2a9d7c5";
    private static final SecretKey secretKey =Keys.hmacShaKeyFor(SECRET.getBytes());



    public void validateToken(String token) {
        extractAllClaims(token);
        if(!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String extractUserMobileNumber(String token) {
        return extractAllClaims(token).getSubject();
    }
}
