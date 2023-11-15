package com.w1zer.security;

import com.w1zer.entity.Profile;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static com.w1zer.constants.SecurityConstants.QUOTES_ROLES;
import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class JwtProvider {
    private static final String USER = "ROLE_USER";
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${w1zer.jwt.access-secret}")
    private String accessSecret;
    @Value("${w1zer.jwt.access-expiration-minutes}")
    private long accessExpirationMinutes;
    @Value("${w1zer.jwt.refresh-secret}")
    private String refreshSecret;
    @Value("${w1zer.jwt.refresh-expiration-minutes}")
    private long refreshExpirationMinutes;

    public String generateAccessToken(Profile profile) {
        Instant accessExpirationInstant = Instant.now().plus(accessExpirationMinutes, MINUTES);
        Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setIssuer(profile.getLogin())
                .addClaims(Map.of(QUOTES_ROLES, USER))
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, accessSecret)
                .compact();
    }

    public String generateRefreshToken(Profile profile) {
        Instant refreshExpirationInstant = Instant.now().plus(refreshExpirationMinutes, MINUTES);
        Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setIssuer(profile.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, refreshSecret)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessSecret);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshSecret);
    }

    private boolean validateToken(String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
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
            logger.error("Unexpected error while validating JWT");
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, accessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, refreshSecret);
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
