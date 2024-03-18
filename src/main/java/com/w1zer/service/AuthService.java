package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.entity.RefreshToken;
import com.w1zer.entity.UserDevice;
import com.w1zer.exception.NotFoundException;
import com.w1zer.exception.ProfileAlreadyExistsException;
import com.w1zer.payload.*;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String INCORRECT_LOGIN_OR_PASSWORD = "Incorrect login or password";
    private static final String REFRESH_TOKEN_IS_INVALID = "Refresh token is invalid";
    private static final String PROFILE_WITH_LOGIN_NOT_FOUND = "Profile with login '%s' not found";
    private static final String PROFILE_WITH_LOGIN_ALREADY_EXISTS = "Profile with login '%s' already exists";
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDeviceService userDeviceService;
    @Value("${w1zer.jwt.access-expiration-hours}")
    private long accessExpirationHours;

    public AuthService(AuthenticationManager authenticationManager, ProfileRepository profileRepository,
                       PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
                       RefreshTokenService refreshTokenService, UserDeviceService userDeviceService) {
        this.authenticationManager = authenticationManager;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.userDeviceService = userDeviceService;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Profile profile = profileRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtFromAuth(authentication);
        userDeviceService.findByProfileId(profile.getId())
                .map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);
        UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.deviceInfo());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setProfile(profile);
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        refreshToken = refreshTokenService.save(refreshToken);
        return new AuthResponse(jwt, refreshToken.getToken(), accessExpirationHours);
    }

    public void register(RegisterRequest registerRequest) {
        validateEmail(registerRequest.email());
        Profile profile = mapToProfile(registerRequest);
        profileRepository.save(profile);
    }

    public AuthResponse refreshJwt(RefreshTokenRequest refreshTokenRequest) {
        String refreshTokenFromRequest = refreshTokenRequest.refreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenFromRequest);
        refreshTokenService.verifyExpiration(refreshToken);
        userDeviceService.verifyRefreshAvailability(refreshToken);
        refreshTokenService.incRefreshCount(refreshToken);
        Profile profile = refreshToken.getUserDevice().getProfile();
        String accessToken = jwtProvider.generateJwtFromProfile(profile);
        return new AuthResponse(accessToken, refreshTokenRequest.refreshToken(), accessExpirationHours);
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !profileRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    private void validateEmail(String email) {
        if (profileRepository.existsByEmail(email)) {
            throw new ProfileAlreadyExistsException("Profile with email %s already exists".formatted(email));
        }
    }

    private Profile mapToProfile(RegisterRequest registerRequest) {
        Profile profile = new Profile();
        profile.setLogin(registerRequest.login());
        profile.setEmail(registerRequest.email());
        profile.setPassword(passwordEncoder.encode(registerRequest.password()));
        profile.setRoles(registerRequest.roles());
        return profileRepository.save(profile);
    }
}
