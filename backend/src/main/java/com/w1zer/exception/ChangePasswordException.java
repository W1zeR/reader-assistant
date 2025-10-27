package com.w1zer.exception;

import java.io.Serial;

public class ChangePasswordException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ChangePasswordException(final String msg) {
        super(msg);
    }
}
