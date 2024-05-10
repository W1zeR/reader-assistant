package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.entity.RefreshToken;
import com.w1zer.entity.UserDevice;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.*;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.security.JwtProvider;
import com.w1zer.security.JwtWithExpiry;
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
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDeviceService userDeviceService;
    private final RoleService roleService;
    private final ProfileValidationService profileValidationService;

    public AuthService(AuthenticationManager authenticationManager, ProfileRepository profileRepository,
                       PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
                       RefreshTokenService refreshTokenService, UserDeviceService userDeviceService,
                       RoleService roleService, ProfileValidationService profileValidationService) {
        this.authenticationManager = authenticationManager;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.userDeviceService = userDeviceService;
        this.roleService = roleService;
        this.profileValidationService = profileValidationService;
    }

    private Profile findByEmail(String email) {
        return profileRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_EMAIL_NOT_FOUND.formatted(email))
        );
    }

    private void deleteRefreshTokenIfPresent(Long idProfile) {
        userDeviceService.findByProfileId(idProfile)
                .map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String email = loginRequest.email().toLowerCase();
        Profile profile = findByEmail(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtWithExpiry jwtWithExpiry = jwtProvider.generateJwtFromAuth(authentication);
        deleteRefreshTokenIfPresent(profile.getId());
        UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.deviceInfo());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setProfile(profile);
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        refreshToken = refreshTokenService.save(refreshToken);
        return new AuthResponse(profile.getId(), jwtWithExpiry.token(), refreshToken.getToken(),
                jwtWithExpiry.expiry());
    }

    public void register(RegisterRequest registerRequest) {
        String email = registerRequest.email().toLowerCase();
        String login = registerRequest.login().toLowerCase();
        profileValidationService.validateLogin(login);
        profileValidationService.validateEmail(email);
        profileRepository.save(createProfile(login, email, registerRequest.password(), registerRequest.roleId()));
    }

    private Profile createProfile(String login, String email, String password, Long roleId) {
        Profile profile = new Profile();
        profile.setLogin(login);
        profile.setEmail(email);
        profile.setPassword(passwordEncoder.encode(password));
        profile.setRoles(Set.of(roleService.findById(roleId)));
        return profile;
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        email = email.toLowerCase();
        Boolean isAvailable = !profileRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        String refreshTokenFromRequest = refreshTokenRequest.refreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenFromRequest);
        refreshTokenService.verifyExpiration(refreshToken);
        userDeviceService.verifyRefreshAvailability(refreshToken);
        refreshTokenService.incRefreshCount(refreshToken);
        Profile profile = refreshToken.getUserDevice().getProfile();
        JwtWithExpiry newAccessToken = jwtProvider.generateJwtFromProfile(profile);
        return new AuthResponse(profile.getId(), newAccessToken.token(), refreshTokenFromRequest,
                newAccessToken.expiry());
    }
}
