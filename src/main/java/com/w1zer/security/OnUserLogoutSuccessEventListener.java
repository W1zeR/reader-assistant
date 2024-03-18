package com.w1zer.security;

import com.w1zer.payload.DeviceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OnUserLogoutSuccessEventListener.class);
    private final JwtProvider jwtProvider;

    public OnUserLogoutSuccessEventListener(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public void onApplicationEvent(@NonNull OnUserLogoutSuccessEvent event) {
        DeviceInfo deviceInfo = event.getLogoutRequest().deviceInfo();
        logger.info(String.format("Logout success event received for user %s for device %s",
                event.getUserEmail(), deviceInfo));
        jwtProvider.markLogoutEventForToken(event);
    }
}
