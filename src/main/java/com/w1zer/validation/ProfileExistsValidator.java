package com.w1zer.validation;

import com.w1zer.repository.ProfileRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProfileExistsValidator implements ConstraintValidator<ProfileExists, Long> {
    private final ProfileRepository profileRepository;

    public ProfileExistsValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean isValid(Long idProfile, ConstraintValidatorContext context) {
        if (idProfile == null) {
            return false;
        }
        return profileRepository.existsById(idProfile);
    }
}
