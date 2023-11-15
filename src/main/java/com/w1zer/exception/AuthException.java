package com.w1zer.exception;

public class AuthException extends RuntimeException {
    public AuthException(final String msg) {
        super(msg);
    }
}