package com.w1zer.mapping;

import com.w1zer.entity.*;
import com.w1zer.payload.*;
import com.w1zer.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuoteMapping {
    private final TagRepository tagRepository;

    public QuoteMapping(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Page<QuoteResponse> mapToQuoteResponsesPage(Page<Quote> quotesPage) {
        return quotesPage.map(this::mapToQuoteResponse);
    }

    public List<QuoteResponse> mapToQuoteResponses(List<Quote> quotes) {
        return quotes
                .stream()
                .map(this::mapToQuoteResponse)
                .collect(Collectors.toList());
    }

    public Set<QuoteResponse> mapToQuoteResponses(Set<Quote> quotes) {
        return quotes
                .stream()
                .map(this::mapToQuoteResponse)
                .collect(Collectors.toSet());
    }

    public QuoteResponse mapToQuoteResponse(Quote quote) {
        Book book = quote.getBook();
        QuoteBookResponse quoteBookResponse = new QuoteBookResponse(book.getId(), book.getTitle(),
                getBookAuthors(book));
        LocalDateTime localDateTime = quote.getChangeDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String changeDate = localDateTime.format(dateTimeFormatter);
        return new QuoteResponse(quote.getId(), quote.getContent(), quoteBookResponse, quote.getStatus(),
                quote.getTags(), mapToQuoteProfileResponse(quote.getProfile()), quote.getLikesCount(),
                changeDate);
    }

    private QuoteProfileResponse mapToQuoteProfileResponse(Profile profile) {
        return new QuoteProfileResponse(profile.getId(), profile.getLogin());
    }

    private Set<BookAuthorResponse> getBookAuthors(Book book) {
        return book.getAuthors()
                .stream()
                .map(a -> new BookAuthorResponse(a.getId(), a.getSurname(), a.getName(), a.getPatronymic()))
                .collect(Collectors.toSet());
    }

    public Book mapToBook(QuoteBookRequest quoteBookRequest) {
        Book book = new Book();
        book.setTitle(quoteBookRequest.title());
        addAuthors(book, mapToAuthors(quoteBookRequest.authors()));
        return book;
    }

    private void addAuthors(Book book, Set<Author> authors) {
        for (Author author : authors) {
            book.addAuthor(author);
        }
    }

    public Set<Author> mapToAuthors(Set<BookAuthorRequest> bookAuthorRequests) {
        return bookAuthorRequests
                .stream()
                .map(this::mapToAuthor)
                .collect(Collectors.toSet());
    }

    private Author mapToAuthor(BookAuthorRequest bookAuthorRequest) {
        Author author = new Author();
        author.setSurname(bookAuthorRequest.surname());
        author.setName(bookAuthorRequest.name());
        author.setPatronymic(bookAuthorRequest.patronymic());
        return author;
    }

    public Set<Tag> mapToTags(Set<TagRequest> tagRequests) {
        return tagRequests
                .stream()
                .map(this::mapToTag)
                .collect(Collectors.toSet());
    }

    public Tag mapToTag(TagRequest tagRequest) {
        return tagRepository.findByName(tagRequest.name())
                .orElse(createTag(tagRequest.name()));
    }

    private Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
