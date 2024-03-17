package com.w1zer.security;

import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LogoutJwtCache {
    private static final Logger logger = LoggerFactory.getLogger(LogoutJwtCache.class);

    private final ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
    private final JwtProvider jwtProvider;

    public LogoutJwtCache(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(1000)
                .build();
    }

    public void markLogoutEventForToken(OnUserLogoutSuccessEvent event) {
        String token = event.getToken();
        if (tokenEventMap.containsKey(token)) {
            logger.info(String.format("Logout token for user %s is already in cache", event.getUserEmail()));
        } else {
            Date tokenExpiryDate = jwtProvider.getExpirationFromJwt(token);
            long ttlForToken = getTTLForToken(tokenExpiryDate);
            logger.info(String.format("Logout token cache set for %s with a TTL of %s seconds. Token will expiry in %s",
                    event.getUserEmail(), ttlForToken, tokenExpiryDate));
            tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS);
        }
    }

    public OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(Date date) {
        Long secondAtExpiry = date.toInstant().getEpochSecond();
        Long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }
}
