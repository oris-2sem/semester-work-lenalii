package com.example.exception;

public class TokenNotFoundException extends NotFoundException{

    private static final String MESSAGE = "Token not found";

    public TokenNotFoundException() {
        super(MESSAGE);
    }
}
