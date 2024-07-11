package com.w1zer.validation;

import com.w1zer.exception.AlreadyExistsException;
import com.w1zer.exception.ChangePasswordException;
import com.w1zer.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileValidator {
    private static final String PROFILE_WITH_EMAIL_ALREADY_EXISTS = "Profile with email '%s' already exists";
    private static final String PROFILE_WITH_LOGIN_ALREADY_EXISTS = "Profile with login '%s' already exists";
    private static final String OLD_PASSWORD_IS_INCORRECT = "Old password is incorrect";

    private final ProfileRepository profileRepository;

    public ProfileValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void validateEmail(String email) {
        if (profileRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(PROFILE_WITH_EMAIL_ALREADY_EXISTS.formatted(email));
        }
    }

    public void validateLogin(String login) {
        if (profileRepository.existsByLogin(login)) {
            throw new AlreadyExistsException(PROFILE_WITH_LOGIN_ALREADY_EXISTS.formatted(login));
        }
    }

    public void validatePassword(String profilePassword, String requestPassword) {
        if (!requestPassword.equals(profilePassword)) {
            throw new ChangePasswordException(OLD_PASSWORD_IS_INCORRECT);
        }
    }
}
