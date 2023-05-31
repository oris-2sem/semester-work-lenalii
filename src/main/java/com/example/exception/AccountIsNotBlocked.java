package com.example.exception;

import java.util.UUID;

public class AccountIsNotBlocked extends BadRequestException{

    private static final String MESSAGE = "Account %s is not blocked";

    public AccountIsNotBlocked(UUID id) {

        super(String.format(MESSAGE, id));
    }
}
