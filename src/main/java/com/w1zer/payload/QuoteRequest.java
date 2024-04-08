package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.CONTENT_LENGTH;

@SuppressWarnings("unused")
public record QuoteRequest(
        @NotBlankIfPresent
        @Size(max = CONTENT_LENGTH)
        String content
) {
}
