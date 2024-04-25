package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import jakarta.validation.Valid;

import java.util.Set;

public record QuoteBookRequest(
        @NotBlankIfPresent
        String title,

        Set<@Valid BookAuthorRequest> authors
) {
}
