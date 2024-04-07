package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import com.w1zer.validation.UniqueProfileEmail;
import com.w1zer.validation.UniqueProfileLogin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.EMAIL_LENGTH;
import static com.w1zer.constants.EntityConstants.LOGIN_LENGTH;
import static com.w1zer.constants.ValidationConstants.*;

public record ProfileRequest(
        @NotBlankIfPresent
        @Size(max = EMAIL_LENGTH, message = EMAIL_SIZE_MESSAGE)
        @Email
        @UniqueProfileEmail
        String email,

        @NotBlankIfPresent
        @Size(min = LOGIN_MIN_SIZE, max = LOGIN_LENGTH, message = LOGIN_SIZE_MESSAGE)
        @UniqueProfileLogin
        String login
) {
}
