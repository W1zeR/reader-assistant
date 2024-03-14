package com.w1zer.constants;

import static com.w1zer.constants.EntityConstants.EMAIL_LENGTH;
import static com.w1zer.constants.EntityConstants.LOGIN_LENGTH;

public final class ValidationConstants {
    public static final String ID_POSITIVE_MESSAGE = "Id must be positive number";

    public static final int LOGIN_MIN_SIZE = 3;
    public static final String LOGIN_SIZE_MESSAGE =
            "Login must contain from " + LOGIN_MIN_SIZE + " to " + LOGIN_LENGTH + " characters";
    public static final String LOGIN_NOT_BLANK_MESSAGE = "Login can't be blank";

    public static final int EMAIL_MIN_SIZE = 5;
    public static final String EMAIL_SIZE_MESSAGE =
            "Email must contain from " + EMAIL_MIN_SIZE + " to " + EMAIL_LENGTH + " characters";
    public static final String EMAIL_NOT_BLANK_MESSAGE = "Email can't be blank";

    public static final int PASSWORD_MIN_SIZE = 5;
    public static final int PASSWORD_MAX_SIZE = 50;
    public static final String PASSWORD_SIZE_MESSAGE =
            "Password must contain from " + PASSWORD_MIN_SIZE + " to " + PASSWORD_MAX_SIZE + " characters";
    public static final String PASSWORD_NOT_BLANK_MESSAGE = "Password can't be blank";

    private ValidationConstants() {
    }
}
