package com.w1zer.payload;

import java.util.Set;

public record QuoteBookRequest(
        String title,

        Set<BookAuthorRequest> authors
) {
}
