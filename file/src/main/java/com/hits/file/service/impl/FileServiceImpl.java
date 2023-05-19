package com.hits.file.service.impl;

import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.service.JwtProvider;
import com.hits.file.config.MinioConfig;
import com.hits.file.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final JwtProvider jwtProvider;

    @PostConstruct
    public void init() {
        log.info("start");
    }
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


