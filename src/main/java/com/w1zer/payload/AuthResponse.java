package com.w1zer.payload;

public record AuthResponse(
        Long userId,

        String accessToken,

        String refreshToken,

        Long accessTokenExpiry
) {
}
