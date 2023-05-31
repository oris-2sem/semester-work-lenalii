package com.example.exception;

import java.util.UUID;

public class DocumentDeleteException extends BadRequestException {

    private static final String MESSAGE = "Document with id %s cannot be deleted";

    public DocumentDeleteException(UUID id) {
        super(String.format(MESSAGE, id));
    }
}
