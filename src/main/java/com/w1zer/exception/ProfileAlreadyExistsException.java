package com.w1zer.exception;

import java.io.Serial;

public class ProfileAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileAlreadyExistsException(final String msg) {
        super(msg);
    }
}
