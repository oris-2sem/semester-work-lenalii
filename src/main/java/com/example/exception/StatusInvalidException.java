package com.example.exception;

public class StatusInvalidException extends BadRequestException {

    private static final String MESSAGE = "Status %s is not valid";

    public StatusInvalidException(String status) {
        super(String.format(MESSAGE, status));
    }
}
