package com.example.demochatgpt.utils;
import com.example.demochatgpt.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    private final JwtConfig jwtConfig;
    private SecretKey key;

    public JwtUtils(JwtConfig config) {
        this.jwtConfig = config;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}