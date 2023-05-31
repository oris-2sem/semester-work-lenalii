package com.example.exception;

public class HrNotFoundException extends NotFoundException {

    private static final String MESSAGE = "Hr not found.";

    public HrNotFoundException() {
        super(MESSAGE);
    }
}
