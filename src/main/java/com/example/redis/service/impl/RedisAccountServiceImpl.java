package com.example.redis.service.impl;

import com.example.model.entity.AccountEntity;
import com.example.redis.exception.RedisAccountNotFoundException;
import com.example.redis.model.RedisAccount;
import com.example.redis.repository.RedisAccountRepository;
import com.example.redis.service.RedisAccountService;
import com.example.repository.AccountRepository;
import com.example.service.JwtBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class RedisAccountServiceImpl implements RedisAccountService {

    private final AccountRepository accountRepository;

    private final JwtBlackListService jwtBlackListService;

    private final RedisAccountRepository redisAccountRepository;

    @Override
    public void addTokenToAccount(AccountEntity account, String accessToken) {

        String redisId = account.getRedisId();

        RedisAccount redisAccount;
        if (redisId != null) {
            redisAccount = redisAccountRepository.findById(redisId).orElseThrow(()-> new RedisAccountNotFoundException(account.getId()));
            if (redisAccount.getTokens() == null) {
                redisAccount.setTokens(new ArrayList<>());
            }

            redisAccount.getTokens().add(accessToken);
        } else {
            redisAccount = RedisAccount.builder()
                    .accountId(account.getId())
                    .tokens(Collections.singletonList(accessToken))
                    .build();
        }
        redisAccountRepository.save(redisAccount);
        account.setRedisId(redisAccount.getId());
        accountRepository.save(account);
    }

    @Override
    public void addAllTokensToBlackList(AccountEntity account) {

        if (account.getRedisId() != null) {
            RedisAccount redisAccount = redisAccountRepository.findById(account.getRedisId())
                    .orElseThrow(() -> new RedisAccountNotFoundException(account.getId()));

            if(redisAccount.getTokens()!=null){
                redisAccount.getTokens().forEach(jwtBlackListService::add);
                redisAccount.getTokens().clear();
                redisAccountRepository.save(redisAccount);
            }
        }
    }
}
