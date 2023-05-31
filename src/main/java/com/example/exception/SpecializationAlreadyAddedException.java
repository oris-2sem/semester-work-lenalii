package com.example.exception;

public class SpecializationAlreadyAddedException extends BadRequestException {

    private static final String MESSAGE = "Specialization %s already added";

    public SpecializationAlreadyAddedException(String specializationName) {
        super(String.format(MESSAGE, specializationName));
    }
}
