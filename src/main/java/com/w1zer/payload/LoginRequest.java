package com.w1zer.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.EMAIL_LENGTH;
import static com.w1zer.constants.ValidationConstants.*;

@SuppressWarnings("unused")
public record LoginRequest(
        @NotBlank(message = EMAIL_NOT_BLANK_MESSAGE)
        @Size(min = EMAIL_MIN_SIZE, max = EMAIL_LENGTH, message = EMAIL_SIZE_MESSAGE)
        String email,

        @NotBlank(message = PASSWORD_NOT_BLANK_MESSAGE)
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_MESSAGE)
        String password,

        @Valid
        @NotNull(message = DEVICE_INFO_NOT_NULL_MESSAGE)
        DeviceInfo deviceInfo
) {
    private static final String DEVICE_INFO_NOT_NULL_MESSAGE = "Device info can't be null";
}
