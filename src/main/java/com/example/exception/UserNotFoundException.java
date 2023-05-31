package com.example.exception;

public class UserNotFoundException extends NotFoundException {

    private static final String MESSAGE = "User with such email not found.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
