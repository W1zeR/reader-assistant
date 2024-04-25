package com.w1zer.controller;

import com.w1zer.entity.Author;
import com.w1zer.entity.Book;
import com.w1zer.payload.BookRequest;
import com.w1zer.payload.QuoteResponse;
import com.w1zer.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.w1zer.constants.ValidationConstants.ID_POSITIVE_MESSAGE;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/{id}")
    public Book replace(@Valid @RequestBody BookRequest bookRequest,
                        @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return bookService.replace(bookRequest, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        bookService.delete(id);
    }

    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody BookRequest bookRequest) {
        bookService.create(bookRequest);
    }

    @GetMapping("/{id}/authors")
    public Set<Author> getAuthors(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return bookService.findById(id).getAuthors();
    }

    @PutMapping("/{bookId}/authors/{authorId}")
    public void addBook(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long bookId,
                        @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long authorId) {
        bookService.addAuthor(bookId, authorId);
    }

    @DeleteMapping("/{bookId}/authors/{authorId}")
    public void removeBook(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long bookId,
                           @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long authorId) {
        bookService.removeAuthor(bookId, authorId);
    }

    @GetMapping("/{id}/quotes")
    public List<QuoteResponse> getQuotes(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return bookService.getQuotes(id);
    }

    @PutMapping("/{bookId}/quotes/{quoteId}")
    public void addQuote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long bookId,
                         @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId) {
        bookService.addQuote(bookId, quoteId);
    }

    @DeleteMapping("/{bookId}/quotes/{quoteId}")
    public void removeQuote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long bookId,
                            @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId) {
        bookService.removeQuote(bookId, quoteId);
    }
}
