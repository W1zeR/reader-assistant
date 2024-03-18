package com.w1zer.payload;

public record AuthResponse(
        String type,

        String accessToken,

        String refreshToken,

        Long expiryHours
) {
    private static final String BEARER = "Bearer";

    public AuthResponse(String accessToken, String refreshToken, Long expiryHours) {
        this(BEARER, accessToken, refreshToken, expiryHours);
    }
}
