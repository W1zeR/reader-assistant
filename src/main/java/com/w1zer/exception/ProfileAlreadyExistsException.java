package com.w1zer.exception;

public class ProfileAlreadyExistsException extends RuntimeException {
    public ProfileAlreadyExistsException(final String msg) {
        super(msg);
    }
}
