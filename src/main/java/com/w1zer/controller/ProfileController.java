package com.w1zer.controller;

import com.w1zer.entity.Profile;
import com.w1zer.entity.Quote;
import com.w1zer.entity.Tag;
import com.w1zer.payload.ApiResponse;
import com.w1zer.payload.ChangePasswordRequest;
import com.w1zer.payload.LogoutRequest;
import com.w1zer.payload.ProfileRequest;
import com.w1zer.security.CurrentUser;
import com.w1zer.security.UserPrincipal;
import com.w1zer.service.ProfileService;
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
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PutMapping("/promoteUserToMod/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void promote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        profileService.promote(id);
    }

    @PutMapping("/demoteModToUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void demote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        profileService.demote(id);
    }

    @GetMapping("/me")
    public Profile getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return profileService.getCurrentUser(userPrincipal);
    }

    @PutMapping("/logout")
    public ApiResponse logout(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody LogoutRequest logoutRequest) {
        return profileService.logout(currentUser, logoutRequest);
    }

    @PutMapping("/changePassword")
    public ApiResponse changePassword(@CurrentUser UserPrincipal currentUser,
                                      @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return profileService.changePassword(currentUser, changePasswordRequest);
    }

    @PatchMapping("/{id}")
    public Profile update(@Valid @RequestBody ProfileRequest profileRequest,
                          @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return profileService.update(profileRequest, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        profileService.delete(id);
    }

    @GetMapping
    public List<Profile> findAll() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    public Profile findById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return profileService.findById(id);
    }

    @GetMapping("/{id}/quotes")
    public List<Quote> getQuotes(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return profileService.findById(id).getQuotes();
    }

    @PutMapping("/{profileId}/quotes/{quoteId}")
    public void addQuote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId,
                         @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId) {
        profileService.addQuote(profileId, quoteId);
    }

    @DeleteMapping("/{profileId}/quotes/{quoteId}")
    public void removeQuote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId,
                            @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId) {
        profileService.removeQuote(profileId, quoteId);
    }

    @GetMapping("/{id}/likedQuotes")
    public Set<Quote> getLikedQuotes(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return profileService.findById(id).getLikedQuotes();
    }

    @PutMapping("/{profileId}/likedQuotes/{quoteId}")
    public void addLikedQuote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId,
                              @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId) {
        profileService.addLikedQuote(profileId, quoteId);
    }

    @DeleteMapping("/{profileId}/likedQuotes/{quoteId}")
    public void removeLikedQuote(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId,
                                 @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long quoteId) {
        profileService.removeLikedQuote(profileId, quoteId);
    }

    @GetMapping("/{id}/interestingTags")
    public Set<Tag> getInterestingTags(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return profileService.findById(id).getInterestingTags();
    }

    @PutMapping("/{profileId}/interestingTags/{tagId}")
    public void addInterestingTag(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId,
                                  @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long tagId) {
        profileService.addInterestingTag(profileId, tagId);
    }

    @DeleteMapping("/{profileId}/interestingTags/{tagId}")
    public void removeInterestingTag(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long profileId,
                                     @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long tagId) {
        profileService.removeInterestingTag(profileId, tagId);
    }
}
