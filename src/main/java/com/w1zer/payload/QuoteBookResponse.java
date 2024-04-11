package com.w1zer.payload;

import java.util.Set;

public record QuoteBookResponse(
        Long id,

        String title,

        Set<BookAuthorResponse> authors
) {
}
