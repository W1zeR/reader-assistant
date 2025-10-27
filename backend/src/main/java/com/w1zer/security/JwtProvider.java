package com.w1zer.security;

import com.w1zer.entity.Profile;
import com.w1zer.exception.InvalidTokenRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.HOURS;

@Component
public class JwtProvider {
    public static final String INVALID_JWT_SIGNATURE = "Invalid JWT signature";
    public static final String MALFORMED_JWT = "Malformed JWT";
    public static final String EXPIRED_JWT = "Expired JWT";
    public static final String UNSUPPORTED_JWT = "Unsupported JWT";
    public static final String JWT_CLAIMS_STRING_IS_EMPTY = "JWT claims string is empty";
    public static final String UNEXPECTED_ERROR_WHILE_VALIDATING_JWT = "Unexpected error while validating JWT";
    public static final String LOGOUT_TOKEN_IS_ALREADY_IN_CACHE = "Logout token for user %s is already in cache";
    public static final String LOGOUT_TOKEN_CACHE_SET =
            "Logout token cache set for %s with a TTL of %s seconds. Token will expiry in %s";
    public static final String TOKEN_CORRESPONDS_TO_AN_ALREADY_LOGGED_OUT_USER =
            "Token corresponds to an already logged out user %s at %s";
    public static final String JWT = "JWT";
    public static final int MAX_SIZE = 1000;
    public static final String AUTH_ISSUER = "auth";
    public static final String PROFILE_ISSUER = "profile";
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private final ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
    @Value("${w1zer.jwt.secret-key}")
    private String secretKey;
    @Value("${w1zer.jwt.access-expiration-hours}")
    private long accessExpirationHours;

    public JwtProvider() {
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(MAX_SIZE)
                .build();
    }

    public JwtWithExpiry generateJwtFromAuth(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Instant now = Instant.now();
        Date issue = Date.from(now);
        Instant expiryInstant = now.plus(accessExpirationHours, HOURS);
        String token = Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuer(AUTH_ISSUER)
                .id(Long.toString(userPrincipal.getId()))
                .issuedAt(issue)
                .expiration(Date.from(expiryInstant))
                .signWith(getSignInKey())
                .compact();
        return new JwtWithExpiry(token, expiryInstant.toEpochMilli());
    }

    public JwtWithExpiry generateJwtFromProfile(Profile profile) {
        Instant now = Instant.now();
        Date issue = Date.from(now);
        Instant expiryInstant = now.plus(accessExpirationHours, HOURS);
        String token = Jwts.builder()
                .subject(profile.getEmail())
                .issuer(PROFILE_ISSUER)
                .id(Long.toString(profile.getId()))
                .issuedAt(issue)
                .expiration(Date.from(expiryInstant))
                .signWith(getSignInKey())
                .compact();
        return new JwtWithExpiry(token, expiryInstant.toEpochMilli());
    }

    public String getUsernameFromJwt(String jwt) {
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
            logger.error(INVALID_JWT_SIGNATURE);
        } catch (MalformedJwtException e) {
            logger.error(MALFORMED_JWT);
        } catch (ExpiredJwtException e) {
            logger.error(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            logger.error(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            logger.error(JWT_CLAIMS_STRING_IS_EMPTY);
        } catch (Exception e) {
            logger.error(UNEXPECTED_ERROR_WHILE_VALIDATING_JWT, e);
        }
        return false;
    }

    public void markLogoutEventForToken(OnUserLogoutSuccessEvent event) {
        String token = event.getToken();
        if (tokenEventMap.containsKey(token)) {
            logger.info(LOGOUT_TOKEN_IS_ALREADY_IN_CACHE.formatted(event.getUserEmail()));
        } else {
            Date tokenExpiryDate = getExpirationFromJwt(token);
            long ttlForToken = getTTLForToken(tokenExpiryDate);
            logger.info(LOGOUT_TOKEN_CACHE_SET.formatted(
                    event.getUserEmail(), ttlForToken, tokenExpiryDate)
            );
            tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS);
        }
    }

    private OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(Date date) {
        Long secondAtExpiry = date.toInstant().getEpochSecond();
        Long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }

    private void validateJwtIsNotForALoggedOutDevice(String jwt) {
        OnUserLogoutSuccessEvent previouslyLoggedOutEvent = getLogoutEventForToken(jwt);
        if (previouslyLoggedOutEvent != null) {
            String userEmail = previouslyLoggedOutEvent.getUserEmail();
            Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            String errorMessage = String.format(TOKEN_CORRESPONDS_TO_AN_ALREADY_LOGGED_OUT_USER,
                    userEmail, logoutEventDate);
            throw new InvalidTokenRequestException(JWT, jwt, errorMessage);
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
