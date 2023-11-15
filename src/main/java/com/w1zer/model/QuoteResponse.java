package com.w1zer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuoteResponse {
    private final Long id;

    private final String content;

    private final String author;

    private final String source;

    private final String description;

    private final Long idProfile;
}
