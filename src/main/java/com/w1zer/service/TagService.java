package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.entity.Quote;
import com.w1zer.entity.Tag;
import com.w1zer.exception.NotFoundException;
import com.w1zer.exception.ProfileAlreadyExistsException;
import com.w1zer.payload.TagRequest;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private static final String TAG_WITH_ID_NOT_FOUND = "Tag with id %s not found";
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    private static final String TAG_WITH_NAME_ALREADY_EXISTS = "Tag with name '%s' already exists";

    private final TagRepository tagRepository;
    private final QuoteRepository quoteRepository;
    private final ProfileRepository profileRepository;

    public TagService(TagRepository tagRepository, QuoteRepository quoteRepository,
                      ProfileRepository profileRepository) {
        this.tagRepository = tagRepository;
        this.quoteRepository = quoteRepository;
        this.profileRepository = profileRepository;
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TAG_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    public Tag update(TagRequest tagRequest, Long id) {
        Tag tag = findById(id);
        if (tagRequest.name() != null) {
            String name = tagRequest.name().toLowerCase();
            validateName(name);
            tag.setName(name);
        }
        return tagRepository.save(tag);
    }

    private void validateName(String name) {
        if (tagRepository.existsByName(name)) {
            throw new ProfileAlreadyExistsException(TAG_WITH_NAME_ALREADY_EXISTS.formatted(name));
        }
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public void create(TagRequest tagRequest) {
        String name = tagRequest.name().toLowerCase();
        validateName(name);
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
    }

    private Quote findQuoteById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addQuote(Long tagId, Long quoteId) {
        Tag tag = findById(tagId);
        Quote quote = findQuoteById(quoteId);
        tag.addQuote(quote);
        tagRepository.save(tag);
    }

    public void removeQuote(Long tagId, Long quoteId) {
        Tag tag = findById(tagId);
        Quote quote = findQuoteById(quoteId);
        tag.removeQuote(quote);
        tagRepository.save(tag);
    }

    private Profile findProfileById(Long id) {
        return profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addInterestedProfile(Long tagId, Long profileId) {
        Tag tag = findById(tagId);
        Profile profile = findProfileById(profileId);
        tag.addInterestedProfile(profile);
        tagRepository.save(tag);
    }

    public void removeInterestedProfile(Long tagId, Long profileId) {
        Tag tag = findById(tagId);
        Profile profile = findProfileById(profileId);
        tag.removeInterestedProfile(profile);
        tagRepository.save(tag);
    }
}
