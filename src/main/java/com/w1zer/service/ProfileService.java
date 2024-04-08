package com.w1zer.service;

import com.w1zer.entity.*;
import com.w1zer.exception.*;
import com.w1zer.payload.ApiResponse;
import com.w1zer.payload.ChangePasswordRequest;
import com.w1zer.payload.LogoutRequest;
import com.w1zer.payload.ProfileRequest;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.repository.TagRepository;
import com.w1zer.security.OnUserLogoutSuccessEvent;
import com.w1zer.security.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProfileService {
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    private static final String PROFILE_MUST_HAVE_2_ROLES_USER_MODERATOR = "Profile must have 2 roles: USER, MODERATOR";
    private static final String PROFILE_MUST_HAVE_1_ROLE_USER = "Profile must have 1 role: USER";
    private static final String INVALID_DEVICE_ID = "Invalid device Id. No matching device found for the given user";
    private static final String USER_HAS_SUCCESSFULLY_LOGGED_OUT = "User has successfully logged out";
    private static final String OLD_PASSWORD_IS_INCORRECT = "Old password is incorrect";
    private static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password changed successfully";
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";
    private static final String TAG_WITH_ID_NOT_FOUND = "Tag with id %s not found";
    private static final String PROFILE_WITH_EMAIL_ALREADY_EXISTS = "Profile with email '%s' already exists";
    private static final String PROFILE_WITH_LOGIN_ALREADY_EXISTS = "Profile with login '%s' already exists";

    private final ProfileRepository profileRepository;
    private final QuoteRepository quoteRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RefreshTokenService refreshTokenService;
    private final TagRepository tagRepository;

    public ProfileService(ProfileRepository profileRepository, QuoteRepository quoteRepository,
                          RoleService roleService, UserDeviceService userDeviceService,
                          ApplicationEventPublisher applicationEventPublisher,
                          RefreshTokenService refreshTokenService, TagRepository tagRepository) {
        this.profileRepository = profileRepository;
        this.quoteRepository = quoteRepository;
        this.roleService = roleService;
        this.userDeviceService = userDeviceService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.refreshTokenService = refreshTokenService;
        this.tagRepository = tagRepository;
    }

    public void promote(Long id) {
        Profile profile = findById(id);
        Set<Role> roles = profile.getRoles();
        Role user = roleService.findByName(RoleName.ROLE_USER);
        if (!(roles.size() == 1 && roles.contains(user))) {
            throw new ProfileRolesException(PROFILE_MUST_HAVE_1_ROLE_USER);
        }
        Role moderator = roleService.findByName(RoleName.ROLE_MODERATOR);
        profile.setRoles(Set.of(user, moderator));
        profileRepository.save(profile);
    }

    public void demote(Long id) {
        Profile profile = findById(id);
        Set<Role> roles = profile.getRoles();
        Role user = roleService.findByName(RoleName.ROLE_USER);
        Role moderator = roleService.findByName(RoleName.ROLE_MODERATOR);
        if (!(roles.size() == 2 && roles.contains(moderator) && roles.contains(user))) {
            throw new ProfileRolesException(PROFILE_MUST_HAVE_2_ROLES_USER_MODERATOR);
        }
        profile.setRoles(Set.of(user));
        profileRepository.save(profile);
    }

    public Profile getCurrentUser(UserPrincipal userPrincipal) {
        Long id = userPrincipal.getId();
        return profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public ApiResponse logout(UserPrincipal currentUser, LogoutRequest logoutRequest) {
        String deviceId = logoutRequest.deviceInfo().getDeviceId();
        UserDevice userDevice = userDeviceService.findByProfileId(currentUser.getId())
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(deviceId, INVALID_DEVICE_ID));
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(),
                logoutRequest.token(), logoutRequest);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return new ApiResponse(USER_HAS_SUCCESSFULLY_LOGGED_OUT);
    }

    public ApiResponse changePassword(UserPrincipal currentUser, ChangePasswordRequest changePasswordRequest) {
        Profile profile = findById(currentUser.getId());
        if (!changePasswordRequest.oldPassword().equals(profile.getPassword())) {
            throw new ChangePasswordException(OLD_PASSWORD_IS_INCORRECT);
        }
        profile.setPassword(changePasswordRequest.newPassword());
        return new ApiResponse(PASSWORD_CHANGED_SUCCESSFULLY);
    }

    public Profile update(ProfileRequest profileRequest, Long id) {
        Profile profile = findById(id);
        if (profileRequest.email() != null) {
            String email = profileRequest.email().toLowerCase();
            validateEmail(email);
            profile.setEmail(email);
        }
        if (profileRequest.login() != null) {
            String login = profileRequest.login().toLowerCase();
            validateLogin(login);
            profile.setLogin(login);
        }
        return profileRepository.save(profile);
    }

    private void validateEmail(String email) {
        if (profileRepository.existsByEmail(email)) {
            throw new ProfileAlreadyExistsException(PROFILE_WITH_EMAIL_ALREADY_EXISTS.formatted(email));
        }
    }

    private void validateLogin(String login) {
        if (profileRepository.existsByLogin(login)) {
            throw new ProfileAlreadyExistsException(PROFILE_WITH_LOGIN_ALREADY_EXISTS.formatted(login));
        }
    }

    public void delete(Long id) {
        profileRepository.deleteById(id);
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    private Quote findQuoteById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addQuote(Long profileId, Long quoteId) {
        Profile profile = findById(profileId);
        Quote quote = findQuoteById(quoteId);
        profile.addQuote(quote);
        profileRepository.save(profile);
    }

    public void removeQuote(Long profileId, Long quoteId) {
        Profile profile = findById(profileId);
        Quote quote = findQuoteById(quoteId);
        profile.removeQuote(quote);
        profileRepository.save(profile);
    }

    public void addLikedQuote(Long profileId, Long quoteId) {
        Profile profile = findById(profileId);
        Quote quote = findQuoteById(quoteId);
        profile.addLikedQuote(quote);
        profileRepository.save(profile);
    }

    public void removeLikedQuote(Long profileId, Long quoteId) {
        Profile profile = findById(profileId);
        Quote quote = findQuoteById(quoteId);
        profile.removeLikedQuote(quote);
        profileRepository.save(profile);
    }

    private Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TAG_WITH_ID_NOT_FOUND.formatted(id))
        );
    }

    public void addInterestingTag(Long profileId, Long tagId) {
        Profile profile = findById(profileId);
        Tag tag = findTagById(tagId);
        profile.addInterestingTag(tag);
        profileRepository.save(profile);
    }

    public void removeInterestingTag(Long profileId, Long tagId) {
        Profile profile = findById(profileId);
        Tag tag = findTagById(tagId);
        profile.removeInterestingTag(tag);
        profileRepository.save(profile);
    }
}
