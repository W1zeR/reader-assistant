package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.DESCRIPTION_LENGTH;

@SuppressWarnings("unused")
public record BookRequest(
        @NotBlankIfPresent
        String title,

        Integer publishingYear,

        @Size(max = DESCRIPTION_LENGTH)
        String description
) {
}
