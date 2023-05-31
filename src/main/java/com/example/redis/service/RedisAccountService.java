package com.example.redis.service;

import com.example.model.entity.AccountEntity;

public interface RedisAccountService {

    void addTokenToAccount(AccountEntity account, String accessToken);

    void addAllTokensToBlackList(AccountEntity account);
}
