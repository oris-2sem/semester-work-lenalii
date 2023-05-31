package com.example.security.filters;

import com.example.security.util.AuthorizationUtil;
import com.example.service.JwtBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.example.security.filters.JwtAuthenticationFilter.COOKIE_NAME;


@Component
@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final RequestMatcher logoutRequest = new AntPathRequestMatcher("/logout", "GET");

    private final JwtBlackListService jwtTokenBlackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(logoutRequest.matches(request)){

            Optional<String> jwt = AuthorizationUtil.getTokenFromCookie(request);
            if(jwt.isPresent()){

                jwtTokenBlackListService.add(jwt.get());
                AuthorizationUtil.deleteCookie(request, response, COOKIE_NAME);
            }
            SecurityContextHolder.clearContext();
            response.sendRedirect("/vacancy");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
