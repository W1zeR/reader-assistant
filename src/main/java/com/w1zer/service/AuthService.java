package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.entity.RefreshToken;
import com.w1zer.payload.LoginRequest;
import com.w1zer.payload.AuthResponse;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.RoleRepository;
import com.w1zer.security.JwtProvider;
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

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ProfileRepository profileRepository, RoleRepository roleRepository,
                       AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService,
                       JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Profile userDetails = (Profile) authentication.getPrincipal();
        String accessToken = jwtProvider.generateJwt(userDetails);
        RefreshToken refreshToken = refreshTokenService.createByIdProfile(userDetails.getId());
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

//    public AuthResponse register(RegisterRequest registerRequest){
//
//    }


}
