package com.w1zer.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.EMAIL_LENGTH;
import static com.w1zer.constants.EntityConstants.LOGIN_LENGTH;
import static com.w1zer.constants.ValidationConstants.*;

public record RegisterRequest(
        @NotBlank(message = EMAIL_NOT_BLANK_MESSAGE)
        @Size(max = EMAIL_LENGTH, message = EMAIL_SIZE_MESSAGE)
        @Email
        String email,

        @NotBlank(message = LOGIN_NOT_BLANK_MESSAGE)
        @Size(min = LOGIN_MIN_SIZE, max = LOGIN_LENGTH, message = LOGIN_SIZE_MESSAGE)
        String login,

        @NotBlank(message = PASSWORD_NOT_BLANK_MESSAGE)
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_MESSAGE)
        String password
) {
    private static final String EMAIL_SIZE_MESSAGE =
            "Email must contain up to " + EMAIL_LENGTH + " characters";
    private static final String EMAIL_NOT_BLANK_MESSAGE = "Email can't be blank";
}
