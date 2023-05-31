package com.example.exception;

import java.util.UUID;

public class FileNotFoundException extends NotFoundException {

    private static final String MESSAGE_WITH_FILE_ID = "File with id %s not found";

    private static final String MESSAGE= "File not found";

    public FileNotFoundException(UUID documentId) {
        super(String.format(MESSAGE_WITH_FILE_ID, documentId));
    }

    public FileNotFoundException() {
        super(MESSAGE);
    }
}
