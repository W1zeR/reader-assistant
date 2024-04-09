package com.w1zer.payload;

import java.util.Set;

public record QuoteBook(
        Long id,

        String title,

        Set<BookAuthor> authors
) {
}
