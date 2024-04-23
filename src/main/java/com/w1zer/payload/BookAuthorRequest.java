package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;

public record BookAuthorRequest(
        @NotBlankIfPresent
        String surname,

        @NotBlankIfPresent
        String name,

        String patronymic
) {
}
