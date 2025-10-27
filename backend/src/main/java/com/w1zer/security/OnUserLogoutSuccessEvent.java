package com.w1zer.security;

import com.w1zer.payload.LogoutRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class OnUserLogoutSuccessEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String userEmail;

    private final String token;

    private final transient LogoutRequest logoutRequest;

    private final Date eventTime;

    public OnUserLogoutSuccessEvent(String userEmail, String token, LogoutRequest logoutRequest) {
        super(userEmail);
        this.userEmail = userEmail;
        this.token = token;
        this.logoutRequest = logoutRequest;
        this.eventTime = Date.from(Instant.now());
    }
}
