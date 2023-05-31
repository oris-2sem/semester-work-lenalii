package com.example.service;

import com.example.security.model.AccessAndRefreshTokens;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RefreshTokenService {

    AccessAndRefreshTokens refreshTokens(HttpServletRequest request, HttpServletResponse response);
}
