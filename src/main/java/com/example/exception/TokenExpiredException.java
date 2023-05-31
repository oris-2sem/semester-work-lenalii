package com.example.exception;

public class TokenExpiredException extends BadRequestException {

    private static final String MESSAGE = "Token expired";

    public TokenExpiredException() {

        super(MESSAGE);
    }
}
