package com.example.exception;

public class BucketCheckExistException extends BadRequestException {

    private static final String MESSAGE = "Couldn't check if the bucket %s exists";

    public BucketCheckExistException(String bucketName) {
        super(String.format(MESSAGE, bucketName));
    }
}
