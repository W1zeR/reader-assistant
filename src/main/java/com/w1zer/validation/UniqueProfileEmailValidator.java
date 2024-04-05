package com.w1zer.validation;

import com.w1zer.repository.ProfileRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueProfileEmailValidator implements ConstraintValidator<UniqueProfileEmail, String> {
    private final ProfileRepository profileRepository;

    public UniqueProfileEmailValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email.isBlank()) {
            return false;
        }
        return !profileRepository.existsByEmail(email);
    }
}
