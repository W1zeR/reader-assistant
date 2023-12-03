package com.w1zer.payload;

public record AuthResponse(
        String type,

        String accessToken,

        String refreshToken
) {
    private static final String BEARER = "Bearer";

    public AuthResponse(String accessToken, String refreshToken) {
        this(BEARER, accessToken, refreshToken);
    }
}
