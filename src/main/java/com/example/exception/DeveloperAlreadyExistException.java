package com.example.exception;

public class DeveloperAlreadyExistException extends BadRequestException {

    private static final String MESSAGE = "Developer with this username already exist";

    public DeveloperAlreadyExistException() {
        super(MESSAGE);
    }
}
