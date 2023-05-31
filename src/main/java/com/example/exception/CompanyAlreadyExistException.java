package com.example.exception;

public class CompanyAlreadyExistException extends BadRequestException{

    private static final String MESSAGE = "Company with this name already exist";

    public CompanyAlreadyExistException() {
        super(MESSAGE);
    }
}
