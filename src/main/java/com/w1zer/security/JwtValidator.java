package com.w1zer.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    @Value("${w1zer.jwt.access-secret}")
    private String accessSecret;
    @Value("${w1zer.jwt.refresh-secret}")
    private String refreshSecret;

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
            logger.error("Unexpected error while validating JWT", e);
        }
        return false;
    }
}
