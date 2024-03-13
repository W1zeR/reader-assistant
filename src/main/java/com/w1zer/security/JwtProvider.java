package com.w1zer.security;

import com.w1zer.entity.Profile;
import com.w1zer.exception.InvalidTokenRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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

    private final LogoutJwtCache logoutJwtCache;

    public JwtProvider(LogoutJwtCache logoutJwtCache) {
        this.logoutJwtCache = logoutJwtCache;
    }

    public String generateJwtFromAuth(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date issue = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plus(accessExpirationMinutes, MINUTES));
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuer("auth")
                .id(Long.toString(userPrincipal.getId()))
                .issuedAt(issue)
                .expiration(expiration)
                .signWith(getSignInKey())
                .compact();
    }

    public String generateJwtFromProfile(Profile profile) {
        Date issue = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plus(accessExpirationMinutes, MINUTES));
        return Jwts.builder()
                .subject(profile.getEmail())
                .issuer("profile")
                .id(Long.toString(profile.getId()))
                .issuedAt(issue)
                .expiration(expiration)
                .signWith(getSignInKey())
                .compact();
    }

    public String getUsernameFromJwt(String jwt){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public Date getExpirationFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getExpiration();
    }

    public boolean validateJwt(String jwt) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwt);
            validateJwtIsNotForALoggedOutDevice(jwt);
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

    private void validateJwtIsNotForALoggedOutDevice(String jwt) {
        OnUserLogoutSuccessEvent previouslyLoggedOutEvent = logoutJwtCache.getLogoutEventForToken(jwt);
        if (previouslyLoggedOutEvent != null) {
            String userEmail = previouslyLoggedOutEvent.getUserEmail();
            Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]",
                    userEmail, logoutEventDate);
            throw new InvalidTokenRequestException("JWT", jwt, errorMessage);
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
