package com.w1zer.model;

public record AuthResponse(
        String type,

        String accessToken,

        String refreshToken
) {
    public AuthResponse(String accessToken, String refreshToken) {
        this("Bearer", accessToken, refreshToken);
    }
}
