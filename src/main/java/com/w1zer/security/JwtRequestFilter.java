package com.w1zer.security;

import com.w1zer.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int BEGIN_INDEX = 7;
    public static final String SETTING_USER_AUTH_ERROR = "Unexpected error while setting user auth";

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && jwtProvider.validateJwt(jwt)) {
                setSecurityContextHolderAuthentication(request, jwt);
            }
        }
        catch (Exception e){
            logger.error(SETTING_USER_AUTH_ERROR, e);
        }
        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith(BEARER)) {
            return jwtToken.substring(BEGIN_INDEX);
        }
        return null;
    }

    private void setSecurityContextHolderAuthentication(HttpServletRequest request, String jwt) {
        String username = jwtProvider.getUsernameFromJwt(jwt);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
