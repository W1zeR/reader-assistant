package com.w1zer.service;

import com.w1zer.entity.Author;
import com.w1zer.entity.Book;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.AuthorRequest;
import com.w1zer.repository.AuthorRepository;
import com.w1zer.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private static final String AUTHOR_WITH_ID_NOT_FOUND = "Author with id %s not found";
    private static final String BOOK_WITH_ID_NOT_FOUND = "Book with id %s not found";

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(AUTHOR_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    public Author update(AuthorRequest authorRequest, Long id) {
        Author author = findById(id);
        if (authorRequest.surname() != null) {
            author.setSurname(authorRequest.surname());
        }
        if (authorRequest.name() != null) {
            author.setName(authorRequest.name());
        }
        if (authorRequest.patronymic() != null) {
            author.setPatronymic(authorRequest.patronymic());
        }
        if (authorRequest.birthday() != null) {
            author.setBirthday(authorRequest.birthday());
        }
        if (authorRequest.death() != null) {
            author.setDeath(authorRequest.death());
        }
        if (authorRequest.description() != null) {
            author.setDescription(authorRequest.description());
        }
        return authorRepository.save(author);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public void create(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setSurname(authorRequest.surname());
        author.setName(authorRequest.name());
        author.setPatronymic(authorRequest.patronymic());
        author.setBirthday(authorRequest.birthday());
        author.setDeath(authorRequest.death());
        author.setDescription(authorRequest.description());
        authorRepository.save(author);
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new NotFoundException(BOOK_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addBook(Long authorId, Long bookId) {
        Author author = findById(authorId);
        Book book = findBookById(bookId);
        author.addBook(book);
        authorRepository.save(author);
    }

    public void removeBook(Long authorId, Long bookId) {
        Author author = findById(authorId);
        Book book = findBookById(bookId);
        author.removeBook(book);
        authorRepository.save(author);
    }
}
