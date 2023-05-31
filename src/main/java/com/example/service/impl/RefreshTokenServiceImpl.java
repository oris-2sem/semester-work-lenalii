package com.example.service.impl;

import com.example.exception.RefreshTokenNotFoundException;
import com.example.model.entity.RefreshTokenEntity;
import com.example.repository.RefreshTokenRepository;
import com.example.security.authentification.RefreshTokenAuthentication;
import com.example.security.details.UserDetailsImpl;
import com.example.security.model.AccessAndRefreshTokens;
import com.example.security.provider.RefreshTokenAuthenticationProvider;
import com.example.security.util.AuthorizationUtil;
import com.example.security.util.JwtUtil;
import com.example.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.security.filters.JwtAuthenticationFilter.COOKIE_NAME;
import static com.example.security.util.constants.Constants.REFRESH_TOKEN_URL;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    private final RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;

    @Transactional
    @Override
    public AccessAndRefreshTokens refreshTokens(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = AuthorizationUtil.getTokenFromHeader(request);

        RefreshTokenEntity refreshTokenEntity =
                refreshTokenRepository.findByValue(refreshToken)
                        .orElseThrow(RefreshTokenNotFoundException::new);

        AccessAndRefreshTokens tokens = jwtUtil
                .generateAccessAndRefreshTokens(new UserDetailsImpl(refreshTokenEntity.getAccount()), REFRESH_TOKEN_URL);
        performAuthentication(refreshToken);

        refreshTokenRepository.delete(refreshTokenEntity);
        AuthorizationUtil.deleteCookie(request, response, COOKIE_NAME);
        Cookie jwt = new Cookie(COOKIE_NAME, tokens.getAccessToken());
        response.addCookie(jwt);
        return tokens;
    }

    private void performAuthentication(String refreshToken) {

        RefreshTokenAuthentication refreshTokenAuthentication = new RefreshTokenAuthentication(refreshToken);
        SecurityContextHolder.getContext()
                .setAuthentication(refreshTokenAuthenticationProvider.authenticate(refreshTokenAuthentication));
    }
}
