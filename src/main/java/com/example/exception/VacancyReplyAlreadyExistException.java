package com.example.exception;

public class VacancyReplyAlreadyExistException extends BadRequestException{

    private static final String MESSAGE = "Reply already exist";

    public VacancyReplyAlreadyExistException() {
        super(MESSAGE);
    }
}
