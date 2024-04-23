package com.w1zer.payload;

import com.w1zer.entity.QuoteStatus;
import com.w1zer.entity.Tag;
import com.w1zer.validation.NotBlankIfPresent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

import static com.w1zer.constants.EntityConstants.CONTENT_LENGTH;

@SuppressWarnings("unused")
public record QuoteRequest(
        @NotBlankIfPresent
        @Size(max = CONTENT_LENGTH)
        String content,

        @Valid
        @NotNull(message = QUOTE_BOOK_REQUEST_NOT_NULL_MESSAGE)
        QuoteBookRequest book,

        Set<@Valid TagRequest> tags
) {
        private static final String QUOTE_BOOK_REQUEST_NOT_NULL_MESSAGE = "Quote book request can't be null";
}
