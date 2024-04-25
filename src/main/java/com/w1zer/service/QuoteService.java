package com.w1zer.service;

import com.w1zer.entity.*;
import com.w1zer.exception.NotFoundException;
import com.w1zer.mapping.QuoteMapping;
import com.w1zer.payload.*;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.repository.TagRepository;
import com.w1zer.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuoteService {
    public static final String QUOTE_STATUS_MUST_BE_PRIVATE = "Quote status must be private";
    public static final String QUOTE_STATUS_MUST_BE_PENDING = "Quote status must be pending";
    public static final String QUOTE_STATUS_MUST_BE_PUBLIC = "Quote status must be public";
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    private static final String TAG_WITH_ID_NOT_FOUND = "Tag with id %s not found";
    private final QuoteRepository quoteRepository;
    private final QuoteStatusService quoteStatusService;
    private final ProfileRepository profileRepository;
    private final TagRepository tagRepository;

    public QuoteService(QuoteRepository quoteRepository, QuoteStatusService quoteStatusService,
                        ProfileRepository profileRepository, TagRepository tagRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteStatusService = quoteStatusService;
        this.profileRepository = profileRepository;
        this.tagRepository = tagRepository;
    }

    public void markPrivateAsPending(Long id) {
        changeQuoteStatus(id, QuoteStatusName.PRIVATE, QuoteStatusName.PENDING);
    }

    public void markPendingAsPublic(Long id) {
        changeQuoteStatus(id, QuoteStatusName.PENDING, QuoteStatusName.PUBLIC);
    }

    public void markPendingAsPrivate(Long id) {
        changeQuoteStatus(id, QuoteStatusName.PENDING, QuoteStatusName.PRIVATE);
    }

    public void markPublicAsPrivate(Long id) {
        changeQuoteStatus(id, QuoteStatusName.PUBLIC, QuoteStatusName.PRIVATE);
    }

    private void changeQuoteStatus(Long id, QuoteStatusName currentStatusName, QuoteStatusName newStatusName) {
        Quote quote = findById(id);
        if (quote.getStatus().getName() != currentStatusName) {
            throw new RuntimeException(getExceptionMessageByQuoteStatusName(currentStatusName));
        }
        quote.setStatus(quoteStatusService.findByName(newStatusName));
        quoteRepository.save(quote);
    }

    private String getExceptionMessageByQuoteStatusName(QuoteStatusName statusName) {
        if (statusName == QuoteStatusName.PRIVATE) {
            return QUOTE_STATUS_MUST_BE_PRIVATE;
        }
        if (statusName == QuoteStatusName.PENDING) {
            return QUOTE_STATUS_MUST_BE_PENDING;
        }
        return QUOTE_STATUS_MUST_BE_PUBLIC;
    }

    public QuoteResponse findQuoteById(Long id) {
        return QuoteMapping.mapToQuoteResponse(quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        ));
    }

    private Quote findById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void delete(Long id) {
        quoteRepository.deleteById(id);
    }

    public QuoteResponse replace(QuoteRequest quoteRequest, Long id) {
        return QuoteMapping.mapToQuoteResponse(quoteRepository.findById(id)
                .map(quote -> {
                    quote.setContent(quoteRequest.content());
                    quote.setBook(mapToBook(quoteRequest.book()));
                    quote.setTags(mapToTags(quoteRequest.tags()));
                    return quoteRepository.save(quote);
                })
                .orElseGet(() -> {
                    Quote quote = new Quote();
                    quote.setId(id);
                    quote.setContent(quoteRequest.content());
                    quote.setBook(mapToBook(quoteRequest.book()));
                    quote.setTags(mapToTags(quoteRequest.tags()));
                    return quoteRepository.save(quote);
                }));
    }

    public List<QuoteResponse> findAllPublic() {
        QuoteStatus pub = quoteStatusService.findByName(QuoteStatusName.PUBLIC);
        return quoteRepository.findAll()
                .stream()
                .map(QuoteMapping::mapToQuoteResponse)
                .filter(quote -> quote.status().equals(pub))
                .collect(Collectors.toList());
    }

    public void create(QuoteRequest quoteRequest, UserPrincipal userPrincipal) {
        Quote quote = new Quote();
        quote.setContent(quoteRequest.content());
        quote.setBook(mapToBook(quoteRequest.book()));
        quote.setTags(mapToTags(quoteRequest.tags()));
        quote.setStatus(quoteStatusService.findByName(QuoteStatusName.PRIVATE));
        quote.setProfile(findByProfileId(userPrincipal.getId()));
        quoteRepository.save(quote);
    }

    private Book mapToBook(QuoteBookRequest quoteBookRequest) {
        Book book = new Book();
        book.setTitle(quoteBookRequest.title());
        book.setAuthors(mapToAuthors(quoteBookRequest.authors()));
        return book;
    }

    private Set<Author> mapToAuthors(Set<BookAuthorRequest> bookAuthorRequests) {
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

    private Set<Tag> mapToTags(Set<TagRequest> tagRequests) {
        return tagRequests
                .stream()
                .map(this::mapToTag)
                .collect(Collectors.toSet());
    }

    private Tag mapToTag(TagRequest tagRequest) {
        return tagRepository.findByName(tagRequest.name())
                .orElse(createTag(tagRequest.name()));
    }

    private Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }

    public Set<Profile> getWhoLiked(Long id) {
        return findById(id).getWhoLiked();
    }

    private Tag findByTagId(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TAG_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addLikedProfile(Long quoteId, Long profileId) {
        Quote quote = findById(quoteId);
        Profile profile = findByProfileId(profileId);
        quote.addLikedProfile(profile);
        quoteRepository.save(quote);
    }

    public void removeLikedProfile(Long quoteId, Long profileId) {
        Quote quote = findById(quoteId);
        Profile profile = findByProfileId(profileId);
        quote.removeLikedProfile(profile);
        quoteRepository.save(quote);
    }

    private Profile findByProfileId(Long id) {
        return profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public Set<Tag> getTags(Long id) {
        return findById(id).getTags();
    }

    public void addTag(Long quoteId, Long tagId) {
        Quote quote = findById(quoteId);
        Tag tag = findByTagId(tagId);
        quote.addTag(tag);
        quoteRepository.save(quote);
    }

    public void removeTag(Long quoteId, Long tagId) {
        Quote quote = findById(quoteId);
        Tag tag = findByTagId(tagId);
        quote.removeTag(tag);
        quoteRepository.save(quote);
    }
}
