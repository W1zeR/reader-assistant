package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.DESCRIPTION_LENGTH;

@SuppressWarnings("unused")
public record BookRequest(
        @NotBlank
        String title,

        Integer publishingYear,

        @Size(max = DESCRIPTION_LENGTH)
        String description
) {
}
