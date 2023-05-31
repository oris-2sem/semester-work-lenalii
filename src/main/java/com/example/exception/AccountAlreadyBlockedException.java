package com.example.exception;

import java.util.UUID;

public class AccountAlreadyBlockedException extends BadRequestException{

    private static final String MESSAGE = "Account %s already blocked";

    public AccountAlreadyBlockedException(UUID id) {

        super(String.format(MESSAGE, id));
    }
}
