package com.w1zer.controller;

import com.w1zer.payload.ProfileRequest;
import com.w1zer.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    //    @GetMapping
//    public List<ProfileResponse> getAll(
//            @RequestParam(required = false)
//            @Size(min = LOGIN_MIN_SIZE, max = LOGIN_LENGTH, message = LOGIN_SIZE_MESSAGE)
//            String login) {
//        return profileService.getAll(login);
//    }
//
//    @GetMapping("/{id}")
//    public ProfileResponse getById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
//        return profileService.getById(id);
//    }
//
//    @PostMapping
//    public ProfileResponse insert(@Valid @RequestBody ProfileRequest profileRequest) {
//        return profileService.insert(profileRequest);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
//        profileService.delete(id);
//    }
//
//    @PutMapping("/{id}")
//    public ProfileResponse update(
//            @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
//            @Valid @RequestBody ProfileRequest profileRequest) {
//        return profileService.update(id, profileRequest);
//    }
    @PutMapping("/promoteUserToModerator")
    @PreAuthorize("hasRole('ADMIN')")
    public void promote(@Valid @RequestBody ProfileRequest profileRequest) {
        profileService.promote(profileRequest);
    }

    @PutMapping("/demoteModeratorToUser")
    @PreAuthorize("hasRole('ADMIN')")
    public void demote(@Valid @RequestBody ProfileRequest profileRequest) {
        profileService.demote(profileRequest);
    }
}
