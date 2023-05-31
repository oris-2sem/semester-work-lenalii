package com.example.exception;

public class UpdateOrDeleteLinkBadRequest extends BadRequestException {

    private static final String MESSAGE = "Link cannot be updated or deleted";

    public UpdateOrDeleteLinkBadRequest() {
        super(MESSAGE);
    }
}
