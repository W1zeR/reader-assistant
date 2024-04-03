package com.w1zer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Documented
public @interface NotBlankIfPresent {

    String message() default "{NotBlankIfPresent.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}