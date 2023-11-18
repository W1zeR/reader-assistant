package com.w1zer.model;

public record QuoteResponse(
        Long id,

        String content,

        String author,

        String source,

        String description,

        Long idProfile
) {
}
