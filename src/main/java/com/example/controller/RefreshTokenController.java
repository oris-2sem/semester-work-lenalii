package com.example.controller;


import com.example.security.model.AccessAndRefreshTokens;
import com.example.security.util.constants.Constants;
import com.example.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping(value = Constants.REFRESH_TOKEN_URL)
    @ResponseStatus(HttpStatus.OK)
    public AccessAndRefreshTokens refreshTokensAndAuthentication(HttpServletRequest request, HttpServletResponse response) {

        return refreshTokenService.refreshTokens(request, response);
    }
}
