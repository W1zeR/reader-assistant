package com.w1zer.controller;

import com.w1zer.model.AuthRequest;
import com.w1zer.model.AuthResponse;
import com.w1zer.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {
    private static final String REFRESH_TOKEN_NOT_BLANK_MESSAGE = "Refresh token can't be blank";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/access")
    public AuthResponse getNewAccessToken(
            @RequestBody @NotBlank(message = REFRESH_TOKEN_NOT_BLANK_MESSAGE) String refreshToken) {
        return authService.getAccessToken(refreshToken);
    }

    @PostMapping("/refresh")
    public AuthResponse getNewRefreshToken(
            @RequestBody @NotBlank(message = REFRESH_TOKEN_NOT_BLANK_MESSAGE) String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
