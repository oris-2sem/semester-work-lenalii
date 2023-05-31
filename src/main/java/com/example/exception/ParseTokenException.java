package com.example.exception;

public class ParseTokenException extends BadRequestException{

    private static final String MESSAGE = "Failed to parse the token";

    public ParseTokenException() {
        super(MESSAGE);
    }
}
