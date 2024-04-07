package com.w1zer.service;

import com.w1zer.entity.Author;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.AuthorRequest;
import com.w1zer.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
	public static final String AUTHOR_WITH_ID_NOT_FOUND = "Author with id %s not found";
	
	private final AuthorRepository authorRepository;
	
	public AuthorService(AuthorRepository authorRepository){
		this.authorRepository = authorRepository;
	}
	
	private Author findById(Long id) {
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
}
