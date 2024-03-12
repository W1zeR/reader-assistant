package com.w1zer.security;

import com.w1zer.entity.Profile;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class JwtProvider {
    @Value("${w1zer.jwt.secret-key}")
    private String secretKey;
    @Value("${w1zer.jwt.access-expiration-minutes}")
    private long accessExpirationMinutes;

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public String generateJwt(Profile profile) {
        String username = profile.getUsername();
        Date issue = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plus(accessExpirationMinutes, MINUTES));
        return Jwts.builder()
                .subject(username)
                .issuedAt(issue)
                .expiration(expiration)
                .signWith(getSignInKey())
                .compact();
    }

    public String getUsernameFromJwt(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty");
        } catch (Exception e) {
            logger.error("Unexpected error while validating JWT", e);
        }
        return false;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
