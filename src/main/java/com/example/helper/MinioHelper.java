package com.example.helper;

import com.example.exception.*;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioHelper {

    private final MinioClient minioClient;

    public boolean checkBucketIsExist(String bucketName) {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder().
                            bucket(bucketName).
                            build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new BucketCheckExistException(bucketName);
        }
    }

    public void removeFromMinio(UUID id, String fileNameInBucket, String bucketName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(fileNameInBucket).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new DocumentDeleteException(id);
        }
    }

    public void createBucketIfNotExist(String bucketName) {

        if (!checkBucketIsExist(bucketName)) {
            try {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                throw new BucketNotCreatedException(bucketName);
            }
        }
    }

    public void uploadFileToMinIOServer(MultipartFile file, String fileNameInBucket, String fileType, String fileName, String bucketName) {

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileNameInBucket)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(fileType)
                    .build());
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            log.error("document not upload to minIO", e);
            throw new FileNotLoadedException(fileName);
        }
    }

    public String getFileLink(String bucketName, String fileName) {

        String link;
        try {
            link = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(2, TimeUnit.MINUTES)
                            .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException | ServerException e) {
            throw new GetFileLinkInMinioException();
        }
        return link;
    }

    public static String createFileNameInBucket(MultipartFile file) {

        String extension = Optional.ofNullable(file.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1))
                .orElseThrow(() -> new IllegalArgumentException("can not define extension"));

        return UUID.randomUUID().toString().concat(".").concat(extension);
    }
}
