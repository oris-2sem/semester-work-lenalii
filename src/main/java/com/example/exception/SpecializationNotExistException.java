package com.example.exception;

import java.util.UUID;

public class SpecializationNotExistException extends BadRequestException {

    private static final String MESSAGE = "Specialization with id %s is not exist";

    public SpecializationNotExistException(UUID id) {
        super(String.format(MESSAGE, id));
    }
}