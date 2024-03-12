package com.w1zer.exception;

import java.io.Serial;

public class AuthException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthException(final String msg) {
        super(msg);
    }
}