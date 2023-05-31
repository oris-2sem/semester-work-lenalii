package com.example.exception;

public class DeveloperNotFoundException extends NotFoundException {

    private static final String MESSAGE = "Developer not found";

    public DeveloperNotFoundException() {
        super(MESSAGE);
    }
}
