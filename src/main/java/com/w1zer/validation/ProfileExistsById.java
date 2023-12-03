package com.w1zer.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ProfileExistsByIdValidator.class)
@Documented
public @interface ProfileExistsById {

    String message() default "{ProfileExists.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}