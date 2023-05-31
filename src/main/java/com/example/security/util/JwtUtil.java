package com.example.security.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


import com.example.exception.UserNotFoundException;
import com.example.model.constant.Role;
import com.example.model.entity.AccountEntity;
import com.example.model.entity.RefreshTokenEntity;
import com.example.redis.service.RedisAccountService;
import com.example.repository.AccountRepository;
import com.example.repository.RefreshTokenRepository;
import com.example.security.details.UserDetailsImpl;
import com.example.security.model.AccessAndRefreshTokens;
import com.example.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${application.token.access.expires.time}")
    private long accessTokenExpiresTime;

    @Value("${application.token.refresh.expires.time}")
    private long refreshTokenExpiresTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final AccountRepository accountRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final RedisAccountService redisAccountService;

    private final AccountService accountService;


    public AccessAndRefreshTokens generateAccessAndRefreshTokens(UserDetailsImpl userDetails, String issuer) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));
        String accessToken = JWT.create()
                .withSubject(userDetails.getAccount().getId().toString())
                .withClaim("role", userDetails.getAccount().getRole().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiresTime))
                .withIssuer(issuer)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(userDetails.getAccount().getId().toString())
                .withClaim("role", userDetails.getAccount().getRole().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiresTime))
                .withIssuer(issuer)
                .sign(algorithm);

        AccessAndRefreshTokens tokens = AccessAndRefreshTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        accountService.saveRefreshToken(userDetails.getAccount().getId(), tokens);

        redisAccountService.addTokenToAccount(userDetails.getAccount(), accessToken);
        return tokens;
    }

    public Authentication buildAuthentication(String token) {

        ParsedToken parsedToken = parse(token);
        UserDetailsImpl userDetails = new UserDetailsImpl(AccountEntity.builder()
                .id(UUID.fromString(parsedToken.getId()))
                .role(Role.valueOf(parsedToken.getRole()))
                .build());
        return new UsernamePasswordAuthenticationToken(userDetails,
                null,
                Collections.singleton(new SimpleGrantedAuthority(parsedToken.getRole())));
    }

    private ParsedToken parse(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();


        DecodedJWT decodedJWT = jwtVerifier.verify(token);


        String role = decodedJWT.getClaim("role").asString();
        String id = decodedJWT.getSubject();

        return ParsedToken.builder()
                .id(id)
                .role(role)
                .build();

    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @lombok.Builder
    private static class ParsedToken {

        private String id;

        private String role;
    }
}
