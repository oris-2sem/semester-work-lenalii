package com.example.redis.repository;

import com.example.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class BlacklistRepositoryRedisImpl implements BlacklistRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String token) {

        redisTemplate.opsForValue().set(token, "");
    }

    @Override
    public boolean exist(String token) {

        Boolean hasToken = redisTemplate.hasKey(token);
        return hasToken != null && hasToken;
    }
}
