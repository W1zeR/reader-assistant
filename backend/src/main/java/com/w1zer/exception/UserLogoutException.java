package com.w1zer.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class UserLogoutException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String COULD_NOT_LOGOUT = "Couldn't log out device with '%s'. '%s'";

    private final String deviceInfo;

    private final String message;

    public UserLogoutException(String deviceInfo, String message) {
        super(COULD_NOT_LOGOUT.formatted(deviceInfo, message));
        this.deviceInfo = deviceInfo;
        this.message = message;
    }
}
