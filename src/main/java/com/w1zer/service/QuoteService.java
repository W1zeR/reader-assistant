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

    public void markAsPending(Long id) {
        Quote quote = findById(id);
        QuoteStatus quoteStatus = quote.getQuoteStatus();
        if (quoteStatus.getName() != QuoteStatusName.PRIVATE) {
            throw new RuntimeException("Quote status must be private");
        }
        QuoteStatus pending = quoteStatusService.findByName(QuoteStatusName.PENDING);
        quote.setQuoteStatus(pending);
        quoteRepository.save(quote);
    }

    public void markAsPublic(Long id) {
        Quote quote = findById(id);
        QuoteStatus quoteStatus = quote.getQuoteStatus();
        if (quoteStatus.getName() != QuoteStatusName.PENDING) {
            throw new RuntimeException("Quote status must be pending");
        }
        QuoteStatus pub = quoteStatusService.findByName(QuoteStatusName.PUBLIC);
        quote.setQuoteStatus(pub);
        quoteRepository.save(quote);
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
                .filter(quote -> quote.quoteStatus().equals(pub))
                .collect(Collectors.toList());
    }

    public void create(QuoteRequest quoteRequest, UserPrincipal userPrincipal) {
        Quote quote = new Quote();
        quote.setContent(quoteRequest.content());
        QuoteStatus pri = quoteStatusService.findByName(QuoteStatusName.PRIVATE);
        quote.setQuoteStatus(pri);
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
