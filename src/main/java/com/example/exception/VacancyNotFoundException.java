package com.example.exception;

public class VacancyNotFoundException extends NotFoundException {

    private static final String MESSAGE = "Vacancy not found";

    public VacancyNotFoundException() {
        super(MESSAGE);
    }

    public VacancyNotFoundException(String message) {
        super(message);
    }
}
