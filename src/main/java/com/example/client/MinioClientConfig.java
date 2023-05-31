package com.example.client;


import com.example.config.MinioConfigurationProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class MinioClientConfig {

    private final MinioConfigurationProperties properties;

    @Bean
    @Primary
    public MinioClient minioClient() {
        return new io.minio.MinioClient.Builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndpoint())
                .build();
    }
}
