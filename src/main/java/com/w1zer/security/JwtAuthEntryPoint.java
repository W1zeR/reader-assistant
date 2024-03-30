package com.w1zer.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    public static final String UNAUTHORIZED_ERROR_MESSAGE = "Unauthorized error. Message - {}";
    public static final String UNAUTHORIZED_ERROR = "Unauthorized error";
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException {

        logger.error(UNAUTHORIZED_ERROR_MESSAGE, e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_ERROR);
    }
}
