package com.w1zer.controller;

import com.w1zer.payload.*;
import com.w1zer.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest);
    }

    @GetMapping("/emailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        return authService.checkEmailAvailability(email);
    }
}
