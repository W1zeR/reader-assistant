package com.w1zer.payload;

import com.w1zer.entity.QuoteStatus;
import com.w1zer.entity.Tag;

import java.util.Set;

@SuppressWarnings("unused")
public record QuoteResponse(
        Long id,

        String content,

        QuoteBookResponse book,

        QuoteStatus status,

        Set<Tag> tags,

        QuoteProfileResponse profile,

        Long likes,

        String changeDate
) {
}
