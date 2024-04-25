package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.TAG_NAME_LENGTH;

@SuppressWarnings("unused")
public record TagRequest(
        @NotBlank
        @Size(max = TAG_NAME_LENGTH)
        String name
) {
}
