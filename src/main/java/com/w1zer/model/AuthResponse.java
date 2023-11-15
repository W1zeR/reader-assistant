package com.w1zer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private final String type = "Bearer";

    private final String accessToken;

    private final String refreshToken;
}
