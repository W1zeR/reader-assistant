package com.w1zer.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@SuppressWarnings("unused")
public record QuoteBookRequest(
        @NotBlank
        String title,

        Set<@Valid BookAuthorRequest> authors
) {
}
