package com.w1zer.service;

import com.w1zer.entity.*;
import com.w1zer.exception.NotFoundException;
import com.w1zer.mapping.QuoteMapping;
import com.w1zer.payload.QuoteRequest;
import com.w1zer.payload.QuoteResponse;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.repository.TagRepository;
import com.w1zer.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    private static final String TAG_WITH_ID_NOT_FOUND = "Tag with id %s not found";
    public static final String QUOTE_STATUS_MUST_BE_PRIVATE = "Quote status must be private";
    public static final String QUOTE_STATUS_MUST_BE_PENDING = "Quote status must be pending";
    public static final String QUOTE_STATUS_MUST_BE_PUBLIC = "Quote status must be public";

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

    private String getExceptionMessageByQuoteStatusName(QuoteStatusName statusName){
        if (statusName == QuoteStatusName.PRIVATE) {
            return QUOTE_STATUS_MUST_BE_PRIVATE;
        }
        if (statusName == QuoteStatusName.PENDING) {
            return QUOTE_STATUS_MUST_BE_PENDING;
        }
        return QUOTE_STATUS_MUST_BE_PUBLIC;
    }

    public QuoteResponse findQuoteResponseById(Long id) {
        return QuoteMapping.mapToQuoteResponse(quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        ));
    }

    public Quote findById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void delete(Long id) {
        quoteRepository.deleteById(id);
    }

    public QuoteResponse update(QuoteRequest QuoteRequest, Long id) {
        Quote quote = findById(id);
        if (QuoteRequest.content() != null) {
            quote.setContent(QuoteRequest.content());
        }
        return QuoteMapping.mapToQuoteResponse(quoteRepository.save(quote));
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
        QuoteStatus pri = quoteStatusService.findByName(QuoteStatusName.PRIVATE);
        quote.setStatus(pri);
        Profile profile = findByProfileId(userPrincipal.getId());
        quote.setProfile(profile);
        quoteRepository.save(quote);
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
