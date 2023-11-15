package com.w1zer.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String msg) {
        super(msg);
    }
}
