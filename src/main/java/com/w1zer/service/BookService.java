package com.w1zer.service;

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
	
	public Book update(UpdateBookRequest updateBookRequest, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new NotFoundException(BOOK_WITH_ID_NOT_FOUND)
        );
        if (updateBookRequest.title() != null) {
            book.setTitle(updateBookRequest.title());
        }
        if (updateBookRequest.publishingYear() != null) {
            book.setPublishingYear(updateBookRequest.publishingYear());
        }
		if (updateBookRequest.description() != null) {
            book.setDescription(updateBookRequest.description());
        }
        return bookRepository.save(book);
    }
}
