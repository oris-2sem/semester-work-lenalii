package com.example.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dto.request.LoginRequest;
import com.example.security.details.UserDetailsImpl;
import com.example.security.model.AccessAndRefreshTokens;
import com.example.security.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.security.util.constants.Constants.AUTHENTICATION_URL;

@Component
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String COOKIE_NAME = "accessToken";

    private static final int COOKIE_MAX_AGE = 3600;

    private final ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(ObjectMapper objectMapper,
                                   JwtUtil jwtUtil,
                                   AuthenticationConfiguration authenticationConfiguration) throws Exception {
        super(authenticationConfiguration.getAuthenticationManager());
        this.setFilterProcessesUrl(AUTHENTICATION_URL);
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("call authentication filter");

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        LoginRequest loginRequest;
        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        String email = loginRequest.getEmail();
        email = (email != null) ? email : "";
        email = email.trim();
        String password = loginRequest.getPassword();
        password = (password != null) ? password : "";

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email,
                password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException{
        log.info("Success authentication");

        writeTokensToCookie(request, response, (UserDetailsImpl) authResult.getPrincipal());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("Unsuccess authentication");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void writeTokensToCookie(HttpServletRequest request, HttpServletResponse response, UserDetailsImpl userDetails) throws IOException {

        response.setContentType("application/json");

        AccessAndRefreshTokens tokens = jwtUtil.generateAccessAndRefreshTokens(
                userDetails, request.getRequestURL().toString());

        Cookie jwtCookie = new Cookie(COOKIE_NAME, tokens.getAccessToken());
        jwtCookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(jwtCookie);
        objectMapper.writeValue(response.getOutputStream(), tokens);
    }
}
