package com.w1zer.service;

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
        Author author = profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND)
        );
        if (updateAuthorRequest.surname() != null) {
            author.setSurname(updateAuthorRequest.surname());
        }
		if (updateAuthorRequest.name() != null) {
            author.setName(updateAuthorRequest.name());
        }
		if (updateAuthorRequest.patronymic() != null) {
            author.setPatronymic(updateAuthorRequest.patronymic);
        }
		if (updateAuthorRequest.birthday() != null) {
            author.setBirthday(updateAuthorRequest.birthday);
        }
		if (updateAuthorRequest.death() != null) {
            author.setDeath(updateAuthorRequest.death);
        }
		if (updateAuthorRequest.description() != null) {
            author.setDescription(updateAuthorRequest.description);
        }
        return profileRepository.save(author);
    }
}
