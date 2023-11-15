package com.w1zer.security;

import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.w1zer.constants.SecurityConstants.QUOTES_ROLES;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int BEGIN_INDEX = 7;

    private final JwtProvider jwtProvider;

    public JwtRequestFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String jwtToken = getJwtTokenFromRequest(request);
        if (jwtToken != null && jwtProvider.validateAccessToken(jwtToken)) {
            setSecurityContextHolderAuthentication(request, jwtToken);
        }
        chain.doFilter(request, response);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith(BEARER)) {
            return jwtToken.substring(BEGIN_INDEX);
        }
        return null;
    }

    private void setSecurityContextHolderAuthentication(HttpServletRequest request, String jwtToken) {
        Claims claims = jwtProvider.getAccessClaims(jwtToken);
        String username = claims.getIssuer();
        String role = claims.get(QUOTES_ROLES).toString();
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username, null, Set.of(new SimpleGrantedAuthority(role)));
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }
}
