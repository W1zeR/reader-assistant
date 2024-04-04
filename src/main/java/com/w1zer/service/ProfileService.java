package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.entity.Role;
import com.w1zer.entity.RoleName;
import com.w1zer.entity.UserDevice;
import com.w1zer.exception.ChangePasswordException;
import com.w1zer.exception.NotFoundException;
import com.w1zer.exception.ProfileRolesException;
import com.w1zer.exception.UserLogoutException;
import com.w1zer.payload.ApiResponse;
import com.w1zer.payload.ChangePasswordRequest;
import com.w1zer.payload.LogoutRequest;
import com.w1zer.payload.UpdateProfileRequest;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.security.OnUserLogoutSuccessEvent;
import com.w1zer.security.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@Service
public class ProfileService {
    public static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id %s not found";
    public static final String PROFILE_MUST_HAVE_2_ROLES_USER_MODERATOR = "Profile must have 2 roles: USER, MODERATOR";
    public static final String PROFILE_MUST_HAVE_1_ROLE_USER = "Profile must have 1 role: USER";
    public static final String INVALID_DEVICE_ID = "Invalid device Id. No matching device found for the given user";
    public static final String USER_HAS_SUCCESSFULLY_LOGGED_OUT = "User has successfully logged out";
    public static final String OLD_PASSWORD_IS_INCORRECT = "Old password is incorrect";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password changed successfully";

    private final ProfileRepository profileRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RefreshTokenService refreshTokenService;

    public ProfileService(ProfileRepository profileRepository,
                          RoleService roleService, UserDeviceService userDeviceService,
                          ApplicationEventPublisher applicationEventPublisher,
                          RefreshTokenService refreshTokenService) {
        this.profileRepository = profileRepository;
        this.roleService = roleService;
        this.userDeviceService = userDeviceService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.refreshTokenService = refreshTokenService;
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

    private Profile findById(Long id) {
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

    public Profile update(UpdateProfileRequest updateProfileRequest, Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND)
        );
        if (updateProfileRequest.email() != null) {
            profile.setEmail(updateProfileRequest.email());
        }
        if (updateProfileRequest.login() != null) {
            profile.setLogin(updateProfileRequest.login());
        }
        return profileRepository.save(profile);
    }

    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}
