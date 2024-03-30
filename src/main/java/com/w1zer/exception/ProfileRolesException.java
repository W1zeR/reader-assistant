package com.w1zer.exception;

import java.io.Serial;

public class ProfileRolesException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileRolesException(final String msg) {
        super(msg);
    }
}
