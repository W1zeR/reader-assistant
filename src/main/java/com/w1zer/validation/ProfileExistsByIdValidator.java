package com.w1zer.validation;

import com.w1zer.repository.ProfileRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProfileExistsByIdValidator implements ConstraintValidator<ProfileExistsById, Long> {
    private final ProfileRepository profileRepository;

    public ProfileExistsByIdValidator(ProfileRepository profileRepository) {
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
