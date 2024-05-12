package com.w1zer.exception;

import java.io.Serial;

public class QuoteChangeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public QuoteChangeException(final String msg) {
        super(msg);
    }
}
