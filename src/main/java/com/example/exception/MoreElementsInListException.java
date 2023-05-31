package com.example.exception;

public class MoreElementsInListException extends BadRequestException{

    private static final String MESSAGE = "More elements in list";

    public MoreElementsInListException() {
        super(MESSAGE);
    }
}
