package com.w1zer.controller;

import com.w1zer.entity.Profile;
import com.w1zer.entity.Tag;
import com.w1zer.payload.QuoteRequest;
import com.w1zer.payload.QuoteResponse;
import com.w1zer.security.CurrentUser;
import com.w1zer.security.UserPrincipal;
import com.w1zer.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.w1zer.constants.ValidationConstants.ID_POSITIVE_MESSAGE;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PutMapping("/{id}/markPrivateAsPending")
    public void markPrivateAsPending(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        quoteService.markPrivateAsPending(id);
    }

    @PutMapping("/{id}/markPendingAsPublic")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void markPendingAsPublic(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        quoteService.markPendingAsPublic(id);
    }

    @PutMapping("/{id}/markPendingAsPrivate")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void markPendingAsPrivate(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        quoteService.markPendingAsPrivate(id);
    }

    @PutMapping("/{id}/markPublicAsPrivate")
    @PreAuthorize("hasRole('ADMIN')")
    public void markPublicAsPrivate(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        quoteService.markPublicAsPrivate(id);
    }

    @PatchMapping("/{id}")
    public QuoteResponse update(@Valid @RequestBody QuoteRequest quoteRequest,
                                @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return quoteService.update(quoteRequest, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        quoteService.delete(id);
    }

    @Operation(summary = "Get quotes with public status")
    @GetMapping
    public List<QuoteResponse> findAllPublic() {
        return quoteService.findAllPublic();
    }

    @GetMapping("/{id}")
    public QuoteResponse findById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return quoteService.findQuoteResponseById(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody QuoteRequest quoteRequest,
                       @CurrentUser UserPrincipal userPrincipal) {
        quoteService.create(quoteRequest, userPrincipal);
    }

    @GetMapping("/{id}/whoLiked")
    public Set<Profile> getWhoLiked(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return quoteService.findById(id).getWhoLiked();
    }

    @PutMapping("/{quoteId}/whoLiked/{profileId}")
    public void addLikedProfile(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                                @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId) {
        quoteService.addLikedProfile(quoteId, profileId);
    }

    @DeleteMapping("/{quoteId}/whoLiked/{profileId}")
    public void removeLikedProfile(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                                   @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId) {
        quoteService.removeLikedProfile(quoteId, profileId);
    }

    @GetMapping("/{id}/tags")
    public Set<Tag> getTags(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return quoteService.findById(id).getTags();
    }

    @PutMapping("/{quoteId}/tags/{tagId}")
    public void addTag(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                       @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long tagId) {
        quoteService.addTag(quoteId, tagId);
    }

    @DeleteMapping("/{quoteId}/tags/{tagId}")
    public void removeTag(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                          @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long tagId) {
        quoteService.removeTag(quoteId, tagId);
    }
}
