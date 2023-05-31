package com.example.exception;

public class BucketNotCreatedException extends BadRequestException {

    private static final String MESSAGE = "Failed to create a bucket with name %s";

    public BucketNotCreatedException(String bucketName) {
        super(String.format(MESSAGE, bucketName));
    }
}
