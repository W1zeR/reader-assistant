package com.w1zer.security;

import com.w1zer.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    @Value("${w1zer.jwt.refresh-secret}")
    private String refreshSecret;
    @Value("${w1zer.jwt.refresh-expiration-minutes}")
    private long refreshExpirationMinutes;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


}
