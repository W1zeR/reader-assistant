package com.w1zer.service;

import com.w1zer.entity.RefreshToken;
import com.w1zer.exception.AuthException;
import com.w1zer.exception.NotFoundException;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.RefreshTokenRepository;
import com.w1zer.service.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class RefreshTokenService {
    private static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token '%s' not found";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token %s was expired";

    @Value("${w1zer.jwt.refresh-expiration-minutes}")
    private long refreshExpirationMinutes;

    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileService profileService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, ProfileRepository profileRepository,
                               ProfileService profileService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.profileService = profileService;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new NotFoundException(REFRESH_TOKEN_NOT_FOUND.formatted(token))
        );
    }

    public RefreshToken createByIdProfile(Long idProfile) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setProfile(profileService.getProfileById(idProfile));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(refreshExpirationMinutes, MINUTES));
        return refreshTokenRepository.save(refreshToken);
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new AuthException(String.format(REFRESH_TOKEN_EXPIRED, token.getToken()));
        }
    }

    public void deleteByIdProfile(Long idProfile) {
        refreshTokenRepository.deleteByProfileId(idProfile);
    }
}
