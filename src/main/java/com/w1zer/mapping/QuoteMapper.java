package com.w1zer.mapping;

import com.w1zer.entity.Quote;
import com.w1zer.model.QuoteRequest;
import com.w1zer.model.QuoteResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuoteMapper {
    public QuoteResponse mapToQuoteResponse(Quote quote) {
        return QuoteResponse
                .builder()
                .id(quote.getId())
                .content(quote.getContent())
//                .author(quote.getAuthor())
//                .source(quote.getSource())
//                .description(quote.getDescription())
//                .idProfile(quote.getIdProfile())
                .build();
    }

    public Quote mapToQuote(QuoteRequest quoteRequest) {
        return Quote
                .builder()
                .content(quoteRequest.getContent())
//                .author(quoteRequest.getAuthor())
//                .source(quoteRequest.getSource())
//                .description(quoteRequest.getDescription())
//                .idProfile(quoteRequest.getIdProfile())
                .build();
    }

    public Quote mapToQuote(Long id, QuoteRequest quoteRequest) {
        return Quote
                .builder()
                .id(id)
                .content(quoteRequest.getContent())
//                .author(quoteRequest.getAuthor())
//                .source(quoteRequest.getSource())
//                .description(quoteRequest.getDescription())
//                .idProfile(quoteRequest.getIdProfile())
                .build();
    }

    public List<QuoteResponse> mapToQuoteResponseList(List<Quote> quotes) {
        return quotes
                .stream()
                .map(this::mapToQuoteResponse)
                .collect(Collectors.toList());
    }
}
