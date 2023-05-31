package com.example.redis.exception;

import com.example.exception.NotFoundException;

import java.util.UUID;

public class RedisAccountNotFoundException extends NotFoundException {

    private static final String MESSAGE = "Account %s not found in redis database";

    public RedisAccountNotFoundException(UUID accountId) {

        super(String.format(MESSAGE, accountId));
    }
}
