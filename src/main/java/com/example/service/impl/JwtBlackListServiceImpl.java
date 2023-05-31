package com.example.service.impl;

import com.example.repository.BlacklistRepository;
import com.example.service.JwtBlackListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class JwtBlackListServiceImpl implements JwtBlackListService {

    private final BlacklistRepository blacklistRepository;

    @Override
    public void add(String token) {

        blacklistRepository.save(token);
    }

    @Override
    public boolean exist(String token) {

        return blacklistRepository.exist(token);
    }
}
