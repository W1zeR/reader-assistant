package com.w1zer.controller;

import com.w1zer.entity.Author;
import com.w1zer.entity.Book;
import com.w1zer.payload.AuthorRequest;
import com.w1zer.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.w1zer.constants.ValidationConstants.ID_POSITIVE_MESSAGE;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/api/authors")
@PreAuthorize("hasRole('ADMIN')")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PutMapping("/{id}")
    public Author replace(@Valid @RequestBody AuthorRequest authorRequest,
                          @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return authorService.replace(authorRequest, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        authorService.delete(id);
    }

    @GetMapping
    public List<Author> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Author findById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return authorService.findById(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody AuthorRequest authorRequest) {
        authorService.create(authorRequest);
    }

    @GetMapping("/{id}/books")
    public Set<Book> getBooks(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return authorService.findById(id).getBooks();
    }

    @PutMapping("/{authorId}/books/{bookId}")
    public void addBook(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long authorId,
                        @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long bookId) {
        authorService.addBook(authorId, bookId);
    }

    @DeleteMapping("/{authorId}/books/{bookId}")
    public void removeBook(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long authorId,
                           @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long bookId) {
        authorService.removeBook(authorId, bookId);
    }
}
