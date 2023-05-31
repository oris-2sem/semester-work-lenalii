package com.example.exception;

public class RefreshTokenNotFoundException extends NotFoundException{

    private static final String MESSAGE = "Refresh token not found";

    public RefreshTokenNotFoundException() {
        super(MESSAGE);
    }
}
