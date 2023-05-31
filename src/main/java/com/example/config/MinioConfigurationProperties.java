package com.example.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfigurationProperties {

    private String secretKey;

    private String accessKey;

    private String endpoint;

    private String bucketNameDocuments;

    private String bucketNamePhotos;
}
