package com.w1zer.controller;

import com.w1zer.entity.Profile;
import com.w1zer.payload.ApiResponse;
import com.w1zer.payload.ChangePasswordRequest;
import com.w1zer.payload.LogoutRequest;
import com.w1zer.security.CurrentUser;
import com.w1zer.security.UserPrincipal;
import com.w1zer.service.ProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.w1zer.constants.ValidationConstants.ID_POSITIVE_MESSAGE;

@RestController
@Validated
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

    @PutMapping("/{id}")
    public Profile update(@Valid @RequestBody Profile profile,
                          @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
        return profileService.update(profile, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        profileService.delete(id);
    }
}
