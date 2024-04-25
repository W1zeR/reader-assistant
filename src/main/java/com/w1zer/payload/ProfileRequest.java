package com.w1zer.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.EMAIL_LENGTH;
import static com.w1zer.constants.EntityConstants.LOGIN_LENGTH;
import static com.w1zer.constants.ValidationConstants.*;

@SuppressWarnings("unused")
public record ProfileRequest(
        @NotBlank
        @Size(max = EMAIL_LENGTH, message = EMAIL_SIZE_MESSAGE)
        @Email
        String email,

        @NotBlank
        @Size(min = LOGIN_MIN_SIZE, max = LOGIN_LENGTH, message = LOGIN_SIZE_MESSAGE)
        String login
) {
}
