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

import java.util.Set;

@Service
public class AuthService {
    private static final String PROFILE_WITH_EMAIL_NOT_FOUND = "Profile with email '%s' not found";
    private static final String PROFILE_WITH_EMAIL_ALREADY_EXISTS = "Profile with email '%s' already exists";
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDeviceService userDeviceService;
    private final RoleService roleService;

    @Value("${w1zer.jwt.access-expiration-hours}")
    private long accessExpirationHours;

    public AuthService(AuthenticationManager authenticationManager, ProfileRepository profileRepository,
                       PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
                       RefreshTokenService refreshTokenService, UserDeviceService userDeviceService,
                       RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.userDeviceService = userDeviceService;
        this.roleService = roleService;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        Profile profile = profileRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_EMAIL_NOT_FOUND.formatted(email))
        );
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.password())
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

    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !profileRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    private void validateEmail(String email) {
        if (profileRepository.existsByEmail(email)) {
            throw new ProfileAlreadyExistsException(PROFILE_WITH_EMAIL_ALREADY_EXISTS.formatted(email));
        }
    }

    private Profile mapToProfile(RegisterRequest registerRequest) {
        Profile profile = new Profile();
        profile.setLogin(registerRequest.login());
        profile.setEmail(registerRequest.email());
        profile.setPassword(passwordEncoder.encode(registerRequest.password()));
        profile.setRoles(Set.of(roleService.findById(registerRequest.roleId())));
        return profileRepository.save(profile);
    }

    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        String token = refreshTokenRequest.refreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(token);
        refreshTokenService.verifyExpiration(refreshToken);
        userDeviceService.verifyRefreshAvailability(refreshToken);
        refreshTokenService.incRefreshCount(refreshToken);
        String newAccessToken = jwtProvider.generateJwtFromProfile(refreshToken.getUserDevice().getProfile());
        return new AuthResponse(newAccessToken, token, accessExpirationHours);
    }
}
