package com.w1zer.service;

import com.w1zer.entity.Book;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.BookRequest;
import com.w1zer.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	public static final String BOOK_WITH_ID_NOT_FOUND = "Book with id %s not found";
	
	private final BookRepository bookRepository;
	
	public BookService(BookRepository bookRepository){
		this.bookRepository = bookRepository;
	}
	
	private Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new NotFoundException(BOOK_WITH_ID_NOT_FOUND.formatted(id))
        );
    }
	
	public void delete(Long id) {
        bookRepository.deleteById(id);
    }
	
	public Book update(BookRequest bookRequest, Long id) {
        Book book = findById(id);
        if (bookRequest.title() != null) {
            book.setTitle(bookRequest.title());
        }
        if (bookRequest.publishingYear() != null) {
            book.setPublishingYear(bookRequest.publishingYear());
        }
		if (bookRequest.description() != null) {
            book.setDescription(bookRequest.description());
        }
        return bookRepository.save(book);
    }
}
