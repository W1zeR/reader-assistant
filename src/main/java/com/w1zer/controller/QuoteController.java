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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public void markPrivateAsPending(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
                                     @CurrentUser UserPrincipal userPrincipal) {
        quoteService.markPrivateAsPending(id, userPrincipal);
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

    @PutMapping("/{id}")
    public QuoteResponse replace(@Valid @RequestBody QuoteRequest quoteRequest,
                                 @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
                                 @CurrentUser UserPrincipal userPrincipal) {
        return quoteService.replace(quoteRequest, id, userPrincipal);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
                       @CurrentUser UserPrincipal userPrincipal) {
        quoteService.delete(id, userPrincipal);
    }

    @Operation(summary = "Get quotes with public status")
    @GetMapping("/public")
    public Page<QuoteResponse> findAllPublic(@RequestParam(required = false) String keyword, Pageable p) {
        return quoteService.findAllPublic(keyword, p);
    }

    @Operation(summary = "Get quotes with pending status")
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<QuoteResponse> findAllPending(@RequestParam(required = false) String keyword, Pageable p) {
        return quoteService.findAllPending(keyword, p);
    }

    @Operation(summary = "Get quotes with private status")
    @GetMapping("/private")
    public Page<QuoteResponse> findAllPrivate(@RequestParam(required = false) String keyword, Pageable p) {
        return quoteService.findAllPrivate(keyword, p);
    }

    @Operation(summary = "Get quotes with public status sort by interesting tags")
    @GetMapping("/publicInteresting")
    public Page<QuoteResponse> findPublicInteresting(@RequestParam(required = false) String keyword,
                                                     Pageable p, @CurrentUser UserPrincipal userPrincipal) {
        return quoteService.findPublicInteresting(keyword, p, userPrincipal);
    }

    @Operation(summary = "Get quotes with pending status sort by interesting tags")
    @GetMapping("/pendingInteresting")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<QuoteResponse> findPendingInteresting(@RequestParam(required = false) String keyword,
                                                      Pageable p, @CurrentUser UserPrincipal userPrincipal) {
        return quoteService.findPendingInteresting(keyword, p, userPrincipal);
    }

    @Operation(summary = "Get quotes with private status sort by interesting tags")
    @GetMapping("/privateInteresting")
    public Page<QuoteResponse> findPrivateInteresting(@RequestParam(required = false) String keyword,
                                                      Pageable p, @CurrentUser UserPrincipal userPrincipal) {
        return quoteService.findPrivateInteresting(keyword, p, userPrincipal);
    }

    @GetMapping("/{id}")
    public QuoteResponse findById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
                                  @CurrentUser UserPrincipal userPrincipal) {
        return quoteService.findQuoteById(id, userPrincipal);
    }

    @PostMapping
    public void create(@Valid @RequestBody QuoteRequest quoteRequest,
                       @CurrentUser UserPrincipal userPrincipal) {
        quoteService.create(quoteRequest, userPrincipal);
    }

    @GetMapping("/{id}/whoLiked")
    public Set<Profile> getWhoLiked(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return quoteService.getWhoLiked(id);
    }

    @PutMapping("/{quoteId}/whoLiked/{profileId}")
    @PreAuthorize("#profileId == authentication.principal.id")
    public void addLikedProfile(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                                @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId) {
        quoteService.addLikedProfile(quoteId, profileId);
    }

    @DeleteMapping("/{quoteId}/whoLiked/{profileId}")
    @PreAuthorize("#profileId == authentication.principal.id")
    public void removeLikedProfile(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                                   @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId) {
        quoteService.removeLikedProfile(quoteId, profileId);
    }

    @GetMapping("/{id}/tags")
    public Set<Tag> getTags(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
                            @CurrentUser UserPrincipal userPrincipal) {
        return quoteService.getTags(id, userPrincipal);
    }

    @PutMapping("/{quoteId}/tags/{tagId}")
    public void addTag(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                       @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long tagId,
                       @CurrentUser UserPrincipal userPrincipal) {
        quoteService.addTag(quoteId, tagId, userPrincipal);
    }

    @DeleteMapping("/{quoteId}/tags/{tagId}")
    public void removeTag(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId,
                          @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long tagId,
                          @CurrentUser UserPrincipal userPrincipal) {
        quoteService.removeTag(quoteId, tagId, userPrincipal);
    }
}
