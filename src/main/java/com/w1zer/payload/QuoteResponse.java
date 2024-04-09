package com.w1zer.payload;

import com.w1zer.entity.QuoteStatus;

@SuppressWarnings("unused")
public record QuoteResponse(
        Long id,

        String content,

        QuoteBook book,

        QuoteStatus quoteStatus
) {
}
