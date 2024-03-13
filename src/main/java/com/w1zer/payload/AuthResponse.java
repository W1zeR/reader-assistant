package com.w1zer.payload;

public record AuthResponse(
        String type,

        String accessToken,

        String refreshToken,

        Long expiryDuration
) {
    private static final String BEARER = "Bearer";

    public AuthResponse(String accessToken, String refreshToken, Long expiryDuration) {
        this(BEARER, accessToken, refreshToken, expiryDuration);
    }
}
