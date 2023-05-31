package com.example.exception;

public class GetFileLinkInMinioException extends BadRequestException{

    private static final String MESSAGE = "Can't get file link in minio server";

    public GetFileLinkInMinioException() {

        super(MESSAGE);
    }
}
