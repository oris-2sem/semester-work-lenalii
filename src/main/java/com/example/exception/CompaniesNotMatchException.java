package com.example.exception;

import java.util.UUID;

public class CompaniesNotMatchException extends BadRequestException {

    private static final String MESSAGE = "Company id is %s, hr company is %s";

    public CompaniesNotMatchException(UUID companyIdVacancy, UUID companyIdHr) {

        super(String.format(MESSAGE, companyIdVacancy.toString(), companyIdHr));
    }
}
