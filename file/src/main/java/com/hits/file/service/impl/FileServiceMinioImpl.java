package com.hits.file.service.impl;

import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.file.config.MinioConfig;
import com.hits.file.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceMinioImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public UUID upload(byte[] file) {
        UUID id = java.util.UUID.randomUUID();;
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .stream(new ByteArrayInputStream(file), file.length, -1)
                    .object(id.toString())
                    .build());
        } catch (Exception e) {
            throw new ExternalServiceErrorException("minio not work");
        }
        return id;
    }

    @Override
    public byte[] download(UUID id) {
        var args = GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(id.toString()).build();

        try (var in = minioClient.getObject(args)){
            return in.readAllBytes();
        }catch (Exception e){
            throw new ExternalServiceErrorException("minio not work");
        }
    }
}


