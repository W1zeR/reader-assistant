package com.w1zer.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class TokenRefreshException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String COULD_NOT_REFRESH_TOKEN = "Couldn't refresh token '%s': '%s'";

    private final String token;

    private final String message;

    public TokenRefreshException(String token, String message) {
        super(COULD_NOT_REFRESH_TOKEN.formatted(token, message));
        this.token = token;
        this.message = message;
    }
}
