package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public record AuthorRequest(
        @NotBlank
        String surname,

        @NotBlank
        String name,

        String patronymic
) {
}
