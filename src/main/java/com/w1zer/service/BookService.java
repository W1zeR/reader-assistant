package com.w1zer.service;

import com.w1zer.entity.Author;
import com.w1zer.entity.Book;
import com.w1zer.entity.Quote;
import com.w1zer.exception.NotFoundException;
import com.w1zer.mapping.QuoteMapping;
import com.w1zer.payload.BookRequest;
import com.w1zer.payload.QuoteResponse;
import com.w1zer.repository.AuthorRepository;
import com.w1zer.repository.BookRepository;
import com.w1zer.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private static final String BOOK_WITH_ID_NOT_FOUND = "Book with id %s not found";
    private static final String AUTHOR_WITH_ID_NOT_FOUND = "Author with id %s not found";
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final QuoteRepository quoteRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, QuoteRepository quoteRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.quoteRepository = quoteRepository;
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new NotFoundException(BOOK_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public Book replace(BookRequest bookRequest, Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookRequest.title());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    Book book = new Book();
                    book.setId(id);
                    book.setTitle(bookRequest.title());
                    return bookRepository.save(book);
                });
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void create(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.title());
        bookRepository.save(book);
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(AUTHOR_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addAuthor(Long bookId, Long authorId) {
        Book book = findById(bookId);
        Author author = findAuthorById(authorId);
        book.addAuthor(author);
        bookRepository.save(book);
    }

    public void removeAuthor(Long bookId, Long authorId) {
        Book book = findById(bookId);
        Author author = findAuthorById(authorId);
        book.removeAuthor(author);
        bookRepository.save(book);
    }

    private Quote findQuoteById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addQuote(Long bookId, Long quoteId) {
        Book book = findById(bookId);
        Quote quote = findQuoteById(quoteId);
        book.addQuote(quote);
        bookRepository.save(book);
    }

    public void removeQuote(Long bookId, Long quoteId) {
        Book book = findById(bookId);
        Quote quote = findQuoteById(quoteId);
        book.removeQuote(quote);
        bookRepository.save(book);
    }

    public List<QuoteResponse> getQuotes(Long id) {
        return QuoteMapping.mapToQuoteResponses(findById(id).getQuotes());
    }
}
