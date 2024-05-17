package com.w1zer.repository;

import com.w1zer.entity.Quote;
import org.springframework.data.jpa.domain.Specification;

public class QuoteSpecs {
    public static Specification<Quote> withContent(String content) {
        return (root, query, builder) -> builder.like(root.get("content"), "%" + content + "%");
    }

    public static Specification<Quote> withBookTitle(String title) {
        return (root, query, builder) -> builder.like(root.join("book").get("title"), "%" + title + "%");
    }

    public static Specification<Quote> withBookAuthorSurname(String surname) {
        return (root, query, builder) -> builder.like(root.join("book").join("author").get("title"), "%" + surname + "%");
    }
}
