package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.ValidationConstants.*;

@SuppressWarnings("unused")
public record ChangePasswordRequest(
        @NotBlank(message = PASSWORD_NOT_BLANK_MESSAGE)
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_MESSAGE)
        String oldPassword,

        @NotBlank(message = PASSWORD_NOT_BLANK_MESSAGE)
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_MESSAGE)
        String newPassword
) {
}
