package com.example.exception;

import java.util.UUID;

public class TagNotExistException extends BadRequestException {

    private static final String MESSAGE_WITH_ID = "Tag with id %s is not exist";

    private static final String MESSAGE= "Tag  is not exist";

    public TagNotExistException() {

        super(MESSAGE);
    }

    public TagNotExistException(UUID id) {
        super(String.format(MESSAGE_WITH_ID, id));
    }
}
