package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.DESCRIPTION_LENGTH;

public record UpdateBookRequest(
        @NotBlankIfPresent
        String title,

        Integer publishingYear,

        @Size(max = DESCRIPTION_LENGTH)
        String description
) {
}
