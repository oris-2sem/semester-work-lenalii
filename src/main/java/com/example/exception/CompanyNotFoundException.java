package com.example.exception;

public class CompanyNotFoundException extends NotFoundException{

    private static final String MESSAGE = "Company not found";

    private static final String MESSAGE_WITH_NAME = "Company whit name %s not found";

    public CompanyNotFoundException() {
        super(MESSAGE);
    }

    public CompanyNotFoundException(String name) {
        super(String.format(MESSAGE_WITH_NAME, name));
    }
}
