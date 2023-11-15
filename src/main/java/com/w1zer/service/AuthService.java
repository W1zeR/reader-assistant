package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.exception.AuthException;
import com.w1zer.model.AuthRequest;
import com.w1zer.model.AuthResponse;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.security.JwtProvider;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String INCORRECT_LOGIN_OR_PASSWORD = "Incorrect login or password";
    private static final String REFRESH_TOKEN_IS_INVALID = "Refresh token is invalid";
    private static final String PROFILE_WITH_LOGIN_NOT_FOUND = "Profile with login '%s' not found";

    private final ProfileRepository profileRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ProfileRepository profileRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest authRequest) {
        Profile profile = profileRepository.findProfileByLogin(authRequest.getLogin()).orElseThrow(
                () -> new AuthException(INCORRECT_LOGIN_OR_PASSWORD)
        );
        if (!passwordEncoder.matches(authRequest.getPassword(), profile.getPassword())) {
            throw new AuthException(INCORRECT_LOGIN_OR_PASSWORD);
        }
        String accessToken = jwtProvider.generateAccessToken(profile);
        String refreshToken = jwtProvider.generateRefreshToken(profile);
        return new AuthResponse(accessToken, refreshToken);
    }

    private Profile getProfileByRefreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException(REFRESH_TOKEN_IS_INVALID);
        }
        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getIssuer();
        return profileRepository.findProfileByLogin(login).orElseThrow(
                () -> new AuthException(PROFILE_WITH_LOGIN_NOT_FOUND.formatted(login))
        );
    }

    public AuthResponse getAccessToken(String refreshToken) {
        Profile profile = getProfileByRefreshToken(refreshToken);
        String accessToken = jwtProvider.generateAccessToken(profile);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        Profile profile = getProfileByRefreshToken(refreshToken);
        String accessToken = jwtProvider.generateAccessToken(profile);
        String newRefreshToken = jwtProvider.generateRefreshToken(profile);
        return new AuthResponse(accessToken, newRefreshToken);
    }
}
