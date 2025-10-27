package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.entity.Quote;
import com.w1zer.entity.QuoteStatusName;
import com.w1zer.entity.Tag;
import com.w1zer.exception.NotFoundException;
import com.w1zer.mapping.QuoteMapping;
import com.w1zer.payload.QuoteRequest;
import com.w1zer.payload.QuoteResponse;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.repository.TagRepository;
import com.w1zer.security.UserPrincipal;
import com.w1zer.validation.QuoteValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
public class QuoteService {
    public static final String INTERESTING = "interesting";
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    private static final String TAG_WITH_ID_NOT_FOUND = "Tag with id %s not found";
    private final QuoteRepository quoteRepository;
    private final QuoteStatusService quoteStatusService;
    private final ProfileRepository profileRepository;
    private final TagRepository tagRepository;
    private final QuoteValidator quoteValidator;
    private final QuoteMapping quoteMapping;

    public QuoteService(QuoteRepository quoteRepository, QuoteStatusService quoteStatusService,
                        ProfileRepository profileRepository, TagRepository tagRepository,
                        QuoteValidator quoteValidator, QuoteMapping quoteMapping) {
        this.quoteRepository = quoteRepository;
        this.quoteStatusService = quoteStatusService;
        this.profileRepository = profileRepository;
        this.tagRepository = tagRepository;
        this.quoteValidator = quoteValidator;
        this.quoteMapping = quoteMapping;
    }

    public void markPrivateAsPending(Long id, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteOwning(id, userPrincipal);
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
        quoteValidator.validateCurrentQuoteStatusName(quote.getStatus().getName(), currentStatusName);
        quote.setStatus(quoteStatusService.findByName(newStatusName));
        quote.setChangeDate(LocalDateTime.now());
        quoteRepository.save(quote);
    }

    public QuoteResponse findQuoteById(Long id, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteIsPublicOrQuoteOwning(id, userPrincipal.getId());
        return quoteMapping.mapToQuoteResponse(quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        ));
    }

    private Quote findById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void delete(Long id, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteOwning(id, userPrincipal);
        quoteValidator.validateQuoteChange(id);
        quoteRepository.deleteById(id);
    }

    public QuoteResponse replace(QuoteRequest quoteRequest, Long id, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteOwning(id, userPrincipal);
        quoteValidator.validateQuoteChange(id);
        return quoteMapping.mapToQuoteResponse(quoteRepository.findById(id)
                .map(quote -> {
                    quote.setContent(quoteRequest.content());
                    quote.setBook(quoteMapping.mapToBook(quoteRequest.book()));
                    quote.setTags(quoteMapping.mapToTags(quoteRequest.tags()));
                    quote.setChangeDate(LocalDateTime.now());
                    return quoteRepository.save(quote);
                })
                .orElseGet(() -> {
                    Quote quote = new Quote();
                    quote.setId(id);
                    quote.setContent(quoteRequest.content());
                    quote.setBook(quoteMapping.mapToBook(quoteRequest.book()));
                    quote.setTags(quoteMapping.mapToTags(quoteRequest.tags()));
                    quote.setChangeDate(LocalDateTime.now());
                    return quoteRepository.save(quote);
                }));
    }

    // Public quotes
    public Page<QuoteResponse> findAllPublic(String keyword, Pageable p, UserPrincipal userPrincipal) {
        if (p.getSort().toString().startsWith(INTERESTING)) {
            Pageable newP = Pageable.ofSize(p.getPageSize()).withPage(p.getPageNumber());
            return findPublicInteresting(keyword, newP, userPrincipal);
        }
        QuoteStatusName pub = QuoteStatusName.PUBLIC;
        if (keyword == null || keyword.isBlank()) {
            return quoteMapping.mapToQuoteResponsesPage(quoteRepository.findAllByStatusNameIs(pub, p));
        }
        return quoteMapping.mapToQuoteResponsesPage(quoteRepository.findPublicOrPendingByStatusNameAndKeyword(
                pub, keyword, p)
        );
    }

    // Pending quotes
    public Page<QuoteResponse> findAllPending(String keyword, Pageable p, UserPrincipal userPrincipal) {
        if (p.getSort().toString().startsWith(INTERESTING)) {
            Pageable newP = Pageable.ofSize(p.getPageSize()).withPage(p.getPageNumber());
            return findPendingInteresting(keyword, newP, userPrincipal);
        }
        QuoteStatusName pending = QuoteStatusName.PENDING;
        if (keyword == null || keyword.isBlank()) {
            return quoteMapping.mapToQuoteResponsesPage(quoteRepository.findAllByStatusNameIs(pending, p));
        }
        return quoteMapping.mapToQuoteResponsesPage(quoteRepository.findPublicOrPendingByStatusNameAndKeyword(
                pending, keyword, p)
        );
    }

    // Private quotes
    public Page<QuoteResponse> findAllPrivate(String keyword, Pageable p, UserPrincipal userPrincipal) {
        if (p.getSort().toString().startsWith(INTERESTING)) {
            Pageable newP = Pageable.ofSize(p.getPageSize()).withPage(p.getPageNumber());
            return findPrivateInteresting(keyword, newP, userPrincipal);
        }
        QuoteStatusName pri = QuoteStatusName.PRIVATE;
        if (keyword == null || keyword.isBlank()) {
            return quoteMapping.mapToQuoteResponsesPage(quoteRepository.findAllByStatusName(pri, p));
        }
        return quoteMapping.mapToQuoteResponsesPage(quoteRepository.findPrivateByStatusNameAndKeyword(
                pri, keyword, p)
        );
    }

    // Sort by interesting tags for public quotes
    private Page<QuoteResponse> findPublicInteresting(String keyword, Pageable p, UserPrincipal userPrincipal) {
        if (keyword == null || keyword.isBlank()) {
            return quoteMapping.mapToQuoteResponsesPage(
                    quoteRepository.findPublicQuotesSortByInterestingTags(userPrincipal.getId(), p)
            );
        }
        return quoteMapping.mapToQuoteResponsesPage(
                quoteRepository.findPublicQuotesByKeywordSortByInterestingTags(userPrincipal.getId(), keyword, p)
        );
    }

    // Sort by interesting tags for pending quotes
    private Page<QuoteResponse> findPendingInteresting(String keyword, Pageable p, UserPrincipal userPrincipal) {
        if (keyword == null || keyword.isBlank()) {
            return quoteMapping.mapToQuoteResponsesPage(
                    quoteRepository.findPendingQuotesSortByInterestingTags(userPrincipal.getId(), p)
            );
        }
        return quoteMapping.mapToQuoteResponsesPage(
                quoteRepository.findPendingQuotesByKeywordSortByInterestingTags(userPrincipal.getId(), keyword, p)
        );
    }

    // Sort by interesting tags for private quotes
    private Page<QuoteResponse> findPrivateInteresting(String keyword, Pageable p, UserPrincipal userPrincipal) {
        if (keyword == null || keyword.isBlank()) {
            return quoteMapping.mapToQuoteResponsesPage(
                    quoteRepository.findPrivateQuotesSortByInterestingTags(userPrincipal.getId(), p)
            );
        }
        return quoteMapping.mapToQuoteResponsesPage(
                quoteRepository.findPrivateQuotesByKeywordSortByInterestingTags(userPrincipal.getId(), keyword, p)
        );
    }

    public void create(QuoteRequest quoteRequest, UserPrincipal userPrincipal) {
        Quote quote = new Quote();
        quote.setContent(quoteRequest.content());
        quote.setBook(quoteMapping.mapToBook(quoteRequest.book()));
        quote.setTags(quoteMapping.mapToTags(quoteRequest.tags()));
        quote.setStatus(quoteStatusService.findByName(QuoteStatusName.PRIVATE));
        quote.setProfile(findByProfileId(userPrincipal.getId()));
        quote.setChangeDate(LocalDateTime.now());
        quoteRepository.save(quote);
    }

    private Tag findByTagId(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TAG_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public Boolean getLikeStatus(Long quoteId, UserPrincipal userPrincipal) {
        return findById(quoteId).getWhoLiked()
                .stream()
                .anyMatch(p -> Objects.equals(p.getId(), userPrincipal.getId()));
    }

    public void addLikedProfile(Long quoteId, Long profileId) {
        quoteValidator.validateQuoteIsPublicOrQuoteOwning(quoteId, profileId);
        Quote quote = findById(quoteId);
        Profile profile = findByProfileId(profileId);
        quote.addLikedProfile(profile);
        quoteRepository.save(quote);
    }

    public void removeLikedProfile(Long quoteId, Long profileId) {
        quoteValidator.validateQuoteIsPublicOrQuoteOwning(quoteId, profileId);
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

    public Set<Tag> getTags(Long id, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteIsPublicOrQuoteOwning(id, userPrincipal.getId());
        return findById(id).getTags();
    }

    public void addTag(Long quoteId, Long tagId, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteOwning(quoteId, userPrincipal);
        Quote quote = findById(quoteId);
        Tag tag = findByTagId(tagId);
        quote.addTag(tag);
        quoteRepository.save(quote);
    }

    public void removeTag(Long quoteId, Long tagId, UserPrincipal userPrincipal) {
        quoteValidator.validateQuoteOwning(quoteId, userPrincipal);
        Quote quote = findById(quoteId);
        Tag tag = findByTagId(tagId);
        quote.removeTag(tag);
        quoteRepository.save(quote);
    }
}
