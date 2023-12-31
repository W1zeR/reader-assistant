package com.w1zer.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int BEGIN_INDEX = 7;

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtProvider jwtProvider, JwtValidator jwtValidator, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.jwtValidator = jwtValidator;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String jwtToken = getJwtFromRequest(request);
            if (jwtToken != null && jwtValidator.validateAccessToken(jwtToken)) {
                setSecurityContextHolderAuthentication(request, jwtToken);
            }
        }
        catch (Exception e){
            logger.error("Unexpected error while setting user auth", e);
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
                userDetails, jwt, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
