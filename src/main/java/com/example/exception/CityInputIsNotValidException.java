package com.example.exception;

public class CityInputIsNotValidException extends BadRequestException{

    private static final String MESSAGE = "Город введен не корректно";

    public CityInputIsNotValidException() {
        super(MESSAGE);
    }
}
