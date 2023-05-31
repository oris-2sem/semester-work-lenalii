package com.example.security.util;



import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.security.filters.JwtAuthenticationFilter.COOKIE_NAME;

@Component
public class AuthorizationUtil {

    private static final String AUTH_HEADER_NAME = "Authorization";

    private static final String BEARER_NAME = "Bearer ";

    public static boolean hasAuthorizationToken(HttpServletRequest request) {

        String header = request.getHeader(AUTH_HEADER_NAME);
        return header != null && header.startsWith(BEARER_NAME);
    }

    public static Optional<String> getTokenFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (!cookie.getName().equals(COOKIE_NAME)) {
                continue;
            }
            // If we find the JWT cookie, return its value
            return Optional.of(cookie.getValue());
        }
        // Return empty if no cookie is found
        return Optional.empty();
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }


    public static String getTokenFromHeader(HttpServletRequest request) {
        var authHeader = request.getHeader(AUTH_HEADER_NAME);

        return authHeader.substring(BEARER_NAME.length());
    }

}
