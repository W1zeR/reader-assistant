package com.w1zer.service;

import com.w1zer.entity.Author;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.UpdateAuthorRequest;
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
	
	public Author update(UpdateAuthorRequest updateAuthorRequest, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(AUTHOR_WITH_ID_NOT_FOUND)
        );
        if (updateAuthorRequest.surname() != null) {
            author.setSurname(updateAuthorRequest.surname());
        }
		if (updateAuthorRequest.name() != null) {
            author.setName(updateAuthorRequest.name());
        }
		if (updateAuthorRequest.patronymic() != null) {
            author.setPatronymic(updateAuthorRequest.patronymic());
        }
		if (updateAuthorRequest.birthday() != null) {
            author.setBirthday(updateAuthorRequest.birthday());
        }
		if (updateAuthorRequest.death() != null) {
            author.setDeath(updateAuthorRequest.death());
        }
		if (updateAuthorRequest.description() != null) {
            author.setDescription(updateAuthorRequest.description());
        }
        return authorRepository.save(author);
    }
}
