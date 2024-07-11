package com.w1zer.service;

import com.w1zer.entity.*;
import com.w1zer.exception.NotFoundException;
import com.w1zer.exception.ProfileRolesException;
import com.w1zer.exception.UserLogoutException;
import com.w1zer.mapping.QuoteMapping;
import com.w1zer.payload.*;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.security.OnUserLogoutSuccessEvent;
import com.w1zer.security.UserPrincipal;
import com.w1zer.validation.ProfileValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProfileService {
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    private static final String PROFILE_MUST_HAVE_2_ROLES_USER_MODERATOR = "Profile must have 2 roles: USER, MODERATOR";
    private static final String PROFILE_MUST_HAVE_1_ROLE_USER = "Profile must have 1 role: USER";
    private static final String DEVICE_INFO = "Browser name: %s, device type: %s";
    private static final String NO_MATCHING_DEVICE = "No matching device found for the given user";
    private static final String USER_HAS_SUCCESSFULLY_LOGGED_OUT = "User has successfully logged out";
    private static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password changed successfully";
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";

    private final ProfileRepository profileRepository;
    private final QuoteRepository quoteRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RefreshTokenService refreshTokenService;
    private final ProfileValidator profileValidator;
    private final QuoteMapping quoteMapping;

    public ProfileService(ProfileRepository profileRepository, QuoteRepository quoteRepository,
                          RoleService roleService, UserDeviceService userDeviceService,
                          ApplicationEventPublisher applicationEventPublisher, RefreshTokenService refreshTokenService,
                          ProfileValidator profileValidator, QuoteMapping quoteMapping) {
        this.profileRepository = profileRepository;
        this.quoteRepository = quoteRepository;
        this.roleService = roleService;
        this.userDeviceService = userDeviceService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.refreshTokenService = refreshTokenService;
        this.profileValidator = profileValidator;
        this.quoteMapping = quoteMapping;
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
        String browserName = logoutRequest.deviceInfo().browserName();
        String deviceType = logoutRequest.deviceInfo().deviceType();
        UserDevice userDevice = userDeviceService.findByProfileId(currentUser.getId())
                .filter(device -> device.getBrowserName().equals(browserName) &&
                        device.getDeviceType().equals(deviceType))
                .orElseThrow(() -> new UserLogoutException(DEVICE_INFO.formatted(browserName, deviceType),
                        NO_MATCHING_DEVICE));
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(),
                logoutRequest.token(), logoutRequest);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return new ApiResponse(USER_HAS_SUCCESSFULLY_LOGGED_OUT);
    }

    public ApiResponse changePassword(UserPrincipal currentUser, ChangePasswordRequest changePasswordRequest) {
        Profile profile = findById(currentUser.getId());
        profileValidator.validatePassword(profile.getPassword(), changePasswordRequest.oldPassword());
        profile.setPassword(changePasswordRequest.newPassword());
        return new ApiResponse(PASSWORD_CHANGED_SUCCESSFULLY);
    }

    public Profile replace(ProfileRequest profileRequest, Long id) {
        return profileRepository.findById(id)
                .map(profile -> {
                    String email = profileRequest.email().toLowerCase();
                    if (!email.equals(profile.getEmail())) {
                        profileValidator.validateEmail(email);
                    }
                    String login = profileRequest.login().toLowerCase();
                    if (!login.equals(profile.getLogin())) {
                        profileValidator.validateLogin(login);
                    }
                    profile.setEmail(email);
                    profile.setLogin(login);
                    return profileRepository.save(profile);
                })
                .orElseGet(() -> {
                    Profile profile = new Profile();
                    profile.setId(id);
                    String email = profileRequest.email().toLowerCase();
                    profileValidator.validateEmail(email);
                    String login = profileRequest.login().toLowerCase();
                    profileValidator.validateLogin(login);
                    profile.setEmail(email);
                    profile.setLogin(login);
                    return profileRepository.save(profile);
                });
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

    public Set<QuoteResponse> getLikedQuotes(Long id) {
        return quoteMapping.mapToQuoteResponses(findById(id).getLikedQuotes());
    }
}
