package com.w1zer.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class InvalidTokenRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String INVALID_TOKEN_REQUEST = "'%s': '%s' token: '%s'";

    private final String tokenType;

    private final String token;

    private final String message;

    public InvalidTokenRequestException(String tokenType, String token, String message) {
        super(INVALID_TOKEN_REQUEST.formatted(message, tokenType, token));
        this.tokenType = tokenType;
        this.token = token;
        this.message = message;
    }
}
