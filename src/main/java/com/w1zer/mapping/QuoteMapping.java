package com.w1zer.mapping;

import com.w1zer.entity.Book;
import com.w1zer.entity.Quote;
import com.w1zer.payload.BookAuthorResponse;
import com.w1zer.payload.QuoteBookResponse;
import com.w1zer.payload.QuoteResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class QuoteMapping {
    public static List<QuoteResponse> mapToQuoteResponse(List<Quote> quotes) {
        return quotes
                .stream()
                .map(QuoteMapping::mapToQuoteResponse)
                .collect(Collectors.toList());
    }

    public static Set<QuoteResponse> mapToQuoteResponse(Set<Quote> quotes) {
        return quotes
                .stream()
                .map(QuoteMapping::mapToQuoteResponse)
                .collect(Collectors.toSet());
    }


    public static QuoteResponse mapToQuoteResponse(Quote quote) {
        Book book = quote.getBook();
        if (book == null) {
            return new QuoteResponse(quote.getId(), quote.getContent(), null, quote.getStatus());
        }
        QuoteBookResponse quoteBookResponse = new QuoteBookResponse(book.getId(), book.getTitle(), getBookAuthors(book));
        return new QuoteResponse(quote.getId(), quote.getContent(), quoteBookResponse, quote.getStatus());
    }

    private static Set<BookAuthorResponse> getBookAuthors(Book book) {
        return book.getAuthors()
                .stream()
                .map(a -> new BookAuthorResponse(a.getId(), a.getSurname(), a.getName(), a.getPatronymic()))
                .collect(Collectors.toSet());
    }

    private QuoteMapping() {

    }
}
