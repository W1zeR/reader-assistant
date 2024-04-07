package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static com.w1zer.constants.EntityConstants.DESCRIPTION_LENGTH;

public record AuthorRequest(
        @NotBlankIfPresent
        String surname,

        @NotBlankIfPresent
        String name,

        String patronymic,

        LocalDate birthday,

        LocalDate death,

        @Size(max = DESCRIPTION_LENGTH)
        String description
) {
}
