package com.example.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.security.util.JwtUtil;
import com.example.service.JwtBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.example.security.util.AuthorizationUtil.getTokenFromCookie;
import static com.example.security.util.constants.Constants.*;


@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final JwtBlackListService jwtBlackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals(AUTHENTICATION_URL)
                || request.getServletPath().equals(REFRESH_TOKEN_URL)
                || request.getServletPath().equals(DEVELOPER_SIGN_UP_URL)
                || request.getServletPath().equals(HR_SIGN_UP_URL)) {
            filterChain.doFilter(request, response);
        } else {
            Optional<String> jwtFromCookie = getTokenFromCookie(request);
            if (jwtFromCookie.isPresent()) {

                String jwt = jwtFromCookie.get();
                if(jwtBlackListService.exist(jwt)){
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                try {
                    Authentication authenticationToken = jwtUtil.buildAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
