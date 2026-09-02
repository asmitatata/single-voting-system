package com.hamropatro.jwt;

import com.hamropatro.dto.userDto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Singleton
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(UserDto user) {
        return Jwts.builder()
                .subject(user.userId().toString())
                .claim("email", user.email())
                .claim("role", user.role())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(UserDto user) {

        return Jwts.builder()
                .subject(user.userId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + 15 * 24 * 60 * 60 * 1000))
                .signWith(getKey())
                .compact();
    }
}