package com.example.exception;

public class FileNotLoadedException extends BadRequestException {

    private static final String MESSAGE = "Failed to upload a file with the name %s";

    public FileNotLoadedException(String fileName) {
        super(String.format(MESSAGE, fileName));
    }
}
