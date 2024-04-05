package com.w1zer.validation;

import com.w1zer.repository.ProfileRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueProfileLoginValidator implements ConstraintValidator<UniqueProfileLogin, String> {
    private final ProfileRepository profileRepository;

    public UniqueProfileLoginValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        if (login.isBlank()) {
            return false;
        }
        return !profileRepository.existsByLogin(login);
    }
}
