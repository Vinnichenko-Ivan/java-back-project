package com.hits.file.config;

import io.minio.MinioClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MinioConfig {
    @Value("minio.access-key")
    private String accessKey;

    @Value("minio.secret-key")
    private String secretKey;

    @Value("minio.url")
    private String url;

    @Getter
    @Value("minio.bucket-name")
    private String bucketName;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .credentials(accessKey, secretKey).
                endpoint(url).
                build();
    }
}
