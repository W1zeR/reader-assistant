package com.w1zer.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @Valid
        @NotNull(message = DEVICE_INFO_NOT_NULL_MESSAGE)
        DeviceInfo deviceInfo,

        @NotNull(message = TOKEN_NOT_NULL_MESSAGE)
        String token
) {
        private static final String DEVICE_INFO_NOT_NULL_MESSAGE = "Device info can't be null";
        private static final String TOKEN_NOT_NULL_MESSAGE = "Token can't be null";
}
