package com.example.exception;

public class UserAlreadyExistException extends BadRequestException {

    private static final String MESSAGE = "User with such email already exist.";

    public UserAlreadyExistException() {

        super(MESSAGE);
    }
}
