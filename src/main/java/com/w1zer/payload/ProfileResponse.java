package com.w1zer.payload;

public record ProfileResponse(
        Long id,

        String email,

        String login,

        Boolean isActive,

        Boolean isEmailVerified
) {
}
