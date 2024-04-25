package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static com.w1zer.constants.EntityConstants.DESCRIPTION_LENGTH;

@SuppressWarnings("unused")
public record AuthorRequest(
        @NotBlank
        String surname,

        @NotBlank
        String name,

        String patronymic,

        LocalDate birthday,

        LocalDate death,

        @Size(max = DESCRIPTION_LENGTH)
        String description
) {
}
