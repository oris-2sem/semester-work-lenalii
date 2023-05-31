package com.example.redis.repository;

import com.example.redis.model.RedisAccount;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface RedisAccountRepository extends KeyValueRepository<RedisAccount, String> {
}
