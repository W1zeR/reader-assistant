package com.w1zer.validation;

import com.w1zer.repository.TagRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueTagNameValidator implements ConstraintValidator<UniqueTagName, String> {
    private final TagRepository tagRepository;

    public UniqueTagNameValidator(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name.isBlank()) {
            return false;
        }
        return !tagRepository.existsByName(name);
    }
}
