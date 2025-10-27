package com.w1zer.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotFoundException(final String msg) {
        super(msg);
    }
}
