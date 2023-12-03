package com.w1zer.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class JwtProvider {
    private static final String AUTHORITIES_CLAIM = "authorities";

    @Value("${w1zer.jwt.access-secret}")
    private String accessSecret;
    @Value("${w1zer.jwt.access-expiration-minutes}")
    private long accessExpirationMinutes;

    public String generateJwt(CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();
        Date issue = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plus(accessExpirationMinutes, MINUTES));
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issue)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, accessSecret)
                .compact();
    }

    public String getUsernameFromJwt(String token){
        return Jwts.parser()
                .setSigningKey(accessSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
