package com.w1zer.security;

public record JwtWithExpiry(
        String token,

        Long expiry
) {
}
