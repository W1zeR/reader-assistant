package com.w1zer.exception;

import java.io.Serial;

public class AlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyExistsException(final String msg) {
        super(msg);
    }
}
