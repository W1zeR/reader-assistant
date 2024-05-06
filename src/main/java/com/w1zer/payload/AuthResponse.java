package com.w1zer.payload;

public record AuthResponse(
        String type,

        String accessToken,

        String refreshToken,

        Long accessTokenExpiry
) {
    private static final String BEARER = "Bearer";

    public AuthResponse(String accessToken, String refreshToken, Long accessTokenExpiry) {
        this(BEARER, accessToken, refreshToken, accessTokenExpiry);
    }
}
