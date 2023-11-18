package com.w1zer.model;

public record ProfileResponse(
        Long id,

        String email,

        String login,

        Boolean isActive,

        Boolean isEmailVerified
) {
}
